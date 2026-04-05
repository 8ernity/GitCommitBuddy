package com.gitcommitbuddy.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.gitcommitbuddy.data.PreferencesManager
import com.gitcommitbuddy.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Reschedules WorkManager reminders after device reboot.
 * Must be @AndroidEntryPoint so Hilt injects PreferencesManager.
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var prefs: PreferencesManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != "android.intent.action.MY_PACKAGE_REPLACED") return

        // goAsync keeps the receiver alive while the coroutine runs
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val snapshot = prefs.getSnapshot()
                if (snapshot.notificationsOn) {
                    ReminderWorker.scheduleDailyReminder(
                        context,
                        snapshot.reminderHour,
                        snapshot.reminderMinute
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}

/**
 * Handles notification action button taps (Open GitHub, Dismiss, Mark Done).
 * Does NOT need Hilt — no injected dependencies.
 */
class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            NotificationHelper.ACTION_OPEN_GITHUB -> {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com")).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            NotificationHelper.ACTION_MARK_DONE -> {
                context.startService(
                    Intent(context, FloatingWidgetService::class.java).apply {
                        action = FloatingWidgetService.ACTION_REFRESH
                    }
                )
            }
            NotificationHelper.ACTION_DISMISS -> {
                /* notification auto-cancels — nothing extra needed */
            }
        }
    }
}
