package com.gitcommitbuddy.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.gitcommitbuddy.data.PreferencesManager
import com.gitcommitbuddy.data.api.ApiResult
import com.gitcommitbuddy.data.repository.GitHubRepository
import com.gitcommitbuddy.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * ReminderWorker — daily commit reminder via WorkManager.
 *
 * Workers:
 *  • ReminderWorker         — periodic, once per day at user-configured time
 *  • FollowUpReminderWorker — one-shot, fires ~2h after daily if not committed
 *  • RemindLaterWorker      — one-shot, fires on user tapping "Remind Me Later"
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: GitHubRepository,
    private val prefs: PreferencesManager,
    private val notifHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME_DAILY        = "daily_reminder"
        const val WORK_NAME_FOLLOWUP     = "followup_reminder"
        const val WORK_NAME_REMIND_LATER = "remind_later"

        // ── Schedule daily periodic reminder ──────────────────────────────────
        fun scheduleDailyReminder(context: Context, reminderHour: Int, reminderMinute: Int) {
            val delay = calculateInitialDelay(reminderHour, reminderMinute)
            val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME_DAILY,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }

        // ── Schedule follow-up after missed daily reminder ────────────────────
        fun scheduleFollowUpReminder(context: Context, delayMinutes: Long = 120) {
            val request = OneTimeWorkRequestBuilder<FollowUpReminderWorker>()
                .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME_FOLLOWUP,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }

        /**
         * "Remind Me Later" — fires a plain notification after [delayMinutes].
         * No network constraint: notification fires even offline.
         */
        fun scheduleRemindLaterNotification(context: Context, delayMinutes: Long = 60) {
            val request = OneTimeWorkRequestBuilder<RemindLaterWorker>()
                .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME_REMIND_LATER,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }

        // ── Cancel helpers ────────────────────────────────────────────────────

        /** Cancel all scheduled workers. */
        fun cancelAll(context: Context) {
            val wm = WorkManager.getInstance(context)
            wm.cancelUniqueWork(WORK_NAME_DAILY)
            wm.cancelUniqueWork(WORK_NAME_FOLLOWUP)
            wm.cancelUniqueWork(WORK_NAME_REMIND_LATER)
        }

        /** Cancel only the follow-up / remind-later workers (used by "Mark as Done"). */
        fun cancelFollowUp(context: Context) {
            val wm = WorkManager.getInstance(context)
            wm.cancelUniqueWork(WORK_NAME_FOLLOWUP)
            wm.cancelUniqueWork(WORK_NAME_REMIND_LATER)
        }

        // ── Delay calculation ─────────────────────────────────────────────────

        /**
         * Returns milliseconds until the next occurrence of [hour]:[minute].
         * If that time has already passed today, targets tomorrow.
         */
        fun calculateInitialDelay(hour: Int, minute: Int): Long {
            val now    = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            // If target is within 60s of now or in the past, push to tomorrow
            if (target.timeInMillis <= now.timeInMillis + 60_000) {
                target.add(Calendar.DAY_OF_YEAR, 1)
            }
            return target.timeInMillis - now.timeInMillis
        }
    }

    // ── doWork ────────────────────────────────────────────────────────────────

    override suspend fun doWork(): Result {
        val snapshot = prefs.getSnapshot()
        if (!snapshot.notificationsOn || snapshot.username.isBlank()) return Result.success()

        return try {
            val result = repository.refreshCommitStatus(snapshot.username, snapshot.token, snapshot.commitLimit)
            when (result) {
                is ApiResult.Success -> {
                    notifHelper.showReminderNotification(result.data.committedToday)
                    // If not committed yet, schedule a follow-up in 2 hours
                    if (!result.data.committedToday) {
                        scheduleFollowUpReminder(applicationContext)
                    }
                    Result.success()
                }
                is ApiResult.Error -> {
                    // Show reminder anyway even if API fails — don't miss the user
                    notifHelper.showReminderNotification(false)
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

// ── FollowUpReminderWorker ────────────────────────────────────────────────────

/**
 * Fires a stronger missed-commit alert if the user still hasn't committed
 * ~2 hours after the daily reminder.
 */
@HiltWorker
class FollowUpReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: GitHubRepository,
    private val prefs: PreferencesManager,
    private val notifHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val snapshot = prefs.getSnapshot()
        if (!snapshot.notificationsOn || snapshot.username.isBlank()) return Result.success()

        return try {
            val result    = repository.refreshCommitStatus(snapshot.username, snapshot.token, snapshot.commitLimit)
            val committed = (result as? ApiResult.Success)?.data?.committedToday ?: false
            if (!committed) notifHelper.showMissedCommitAlert()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

// ── RemindLaterWorker ─────────────────────────────────────────────────────────

/**
 * Simple one-shot worker for "Remind Me Later".
 * Posts a standard reminder notification without a network call — we don't
 * need fresh data, just the nudge.
 */
@HiltWorker
class RemindLaterWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        notifHelper.showRemindLaterNotification()
        return Result.success()
    }
}
