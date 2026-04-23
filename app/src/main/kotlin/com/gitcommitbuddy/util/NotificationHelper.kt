package com.gitcommitbuddy.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gitcommitbuddy.R
import com.gitcommitbuddy.service.NotificationActionReceiver
import com.gitcommitbuddy.ui.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NotificationHelper
 * ──────────────────
 * Centralises notification channel creation and all notification builders.
 *
 * Channels (IDs match the spec):
 *  ┌─────────────────────────────┬──────────────────┬─────────────────────────┐
 *  │ Channel name                │ ID               │ Purpose                 │
 *  ├─────────────────────────────┼──────────────────┼─────────────────────────┤
 *  │ GitCommit Buddy Widget      │ floating_service_ │ Persistent FG service   │
 *  │                             │ channel          │ notification (silent)   │
 *  ├─────────────────────────────┼──────────────────┼─────────────────────────┤
 *  │ Commit Reminders            │ reminder_channel  │ Daily commit reminder   │
 *  │                             │                  │ (high priority)         │
 *  ├─────────────────────────────┼──────────────────┼─────────────────────────┤
 *  │ Missed Commit Alert         │ gcb_alert        │ Urgent follow-up alert  │
 *  │                             │                  │ (max + vibration)       │
 *  └─────────────────────────────┴──────────────────┴─────────────────────────┘
 */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        // ── Channel IDs (named as per spec) ───────────────────────────────────
        const val CHANNEL_FOREGROUND = "floating_service_channel"
        const val CHANNEL_REMINDER   = "reminder_channel"
        const val CHANNEL_ALERT      = "gcb_alert"

        // ── Notification IDs ──────────────────────────────────────────────────
        const val NOTIF_ID_FOREGROUND   = 1003
        const val NOTIF_ID_REMINDER     = 1001
        const val NOTIF_ID_ALERT        = 1002
        const val NOTIF_ID_REMIND_LATER = 1004

        // ── Action strings for NotificationActionReceiver ─────────────────────
        const val ACTION_OPEN_GITHUB  = "com.gitcommitbuddy.OPEN_GITHUB"
        const val ACTION_DISMISS      = "com.gitcommitbuddy.DISMISS"
        const val ACTION_MARK_DONE    = "com.gitcommitbuddy.MARK_DONE"
    }

    // ── Channel creation ──────────────────────────────────────────────────────

    /**
     * Creates all notification channels. Safe to call multiple times —
     * the OS is a no-op if the channel already exists.
     */
    fun createNotificationChannels() {
        val nm = context.getSystemService(NotificationManager::class.java)

        // Foreground service channel — silent, no badge
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_FOREGROUND,
                "GitCommit Buddy Widget",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = "Shown while the floating widget is active"
                setShowBadge(false)
            }
        )

        // Daily reminder channel — high priority, lights + vibration
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_REMINDER,
                "Commit Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily reminder to push code to GitHub"
                enableLights(true)
                enableVibration(true)
            }
        )

        // Missed-commit alert channel — max priority, custom vibration pattern
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ALERT,
                "Missed Commit Alert",
                NotificationManager.IMPORTANCE_MAX
            ).apply {
                description = "Urgent alert when no commit detected"
                enableLights(true)
                lightColor = 0xFFFF6B35.toInt()
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500)
            }
        )
    }

    // ── Notification builders ─────────────────────────────────────────────────

    /**
     * Persistent foreground service notification.
     * Title: "GitCommitBuddy Running" as per spec.
     */
    fun buildForegroundNotification(): Notification =
        NotificationCompat.Builder(context, CHANNEL_FOREGROUND)
            .setSmallIcon(R.drawable.ic_notification_commit)
            .setContentTitle("GitCommitBuddy is Active")
            .setContentText("Monitoring your daily streak")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setGroup("Gcb_Foreground_Group")
            .setGroupSummary(false)
            .setOngoing(true)
            .setLocalOnly(true)
            .setContentIntent(openAppPendingIntent())
            .build()

    /**
     * Daily reminder notification.
     * Title/body: "GitCommitBuddy 🚀" / "Don't break your streak! Make a commit today."
     * (spec text), with an adapted variant if the user already committed.
     */
    fun buildReminderNotification(committedToday: Boolean): Notification {
        val title = "GitCommitBuddy 🚀"
        val body  = if (committedToday)
            "✅ You already committed today! Great work 🔥"
        else
            "Don't break your streak! Make a commit today."

        return NotificationCompat.Builder(context, CHANNEL_REMINDER)
            .setSmallIcon(R.drawable.ic_notification_commit)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent())
            .addAction(openGitHubAction())
            .addAction(dismissAction())
            .build()
    }

    /**
     * "Remind Me Later" notification — plain nudge without API data.
     */
    fun buildRemindLaterNotification(): Notification =
        NotificationCompat.Builder(context, CHANNEL_REMINDER)
            .setSmallIcon(R.drawable.ic_notification_commit)
            .setContentTitle("GitCommitBuddy 🚀")
            .setContentText("Don't break your streak! Make a commit today.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent())
            .addAction(openGitHubAction())
            .build()

    /**
     * Missed-commit alert — stronger vibration, urgent priority.
     */
    fun buildMissedCommitAlert(): Notification {
        val messages    = listOf("⚠️ No commit today yet!", "🚨 Streak at risk! Commit now.", "💀 Don't break the streak!")
        val motivations = listOf(
            "Even a tiny fix counts. Open GitHub now!",
            "10 minutes of code keeps the streak alive.",
            "Your future self will thank you. Push something!"
        )
        val idx = (System.currentTimeMillis() / 1000 % 3).toInt()

        return NotificationCompat.Builder(context, CHANNEL_ALERT)
            .setSmallIcon(R.drawable.ic_notification_alert)
            .setContentTitle(messages[idx])
            .setContentText(motivations[idx])
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500, 200, 500))
            .setContentIntent(openAppPendingIntent())
            .addAction(openGitHubAction())
            .build()
    }

    // ── Show helpers ──────────────────────────────────────────────────────────

    fun showReminderNotification(committedToday: Boolean) {
        NotificationManagerCompat.from(context)
            .notify(NOTIF_ID_REMINDER, buildReminderNotification(committedToday))
    }

    fun showRemindLaterNotification() {
        NotificationManagerCompat.from(context)
            .notify(NOTIF_ID_REMIND_LATER, buildRemindLaterNotification())
    }

    fun showMissedCommitAlert() {
        NotificationManagerCompat.from(context)
            .notify(NOTIF_ID_ALERT, buildMissedCommitAlert())
    }

    fun cancelAll() {
        NotificationManagerCompat.from(context).cancelAll()
    }

    // ── Private PendingIntent / Action builders ───────────────────────────────

    private fun openAppPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun openGitHubAction(): NotificationCompat.Action {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_OPEN_GITHUB
        }
        val pi = PendingIntent.getBroadcast(
            context, 10, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Action(R.drawable.ic_github, "Open GitHub", pi)
    }

    private fun dismissAction(): NotificationCompat.Action {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_DISMISS
        }
        val pi = PendingIntent.getBroadcast(
            context, 11, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Action(0, "Dismiss", pi)
    }
}
