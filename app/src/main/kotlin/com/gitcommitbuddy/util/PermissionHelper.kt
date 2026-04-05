package com.gitcommitbuddy.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher

/**
 * Centralises all permission-checking logic.
 * Keeps Activities clean by moving this boilerplate here.
 */
object PermissionHelper {

    // ── Overlay (SYSTEM_ALERT_WINDOW) ─────────────────────────────────────────

    /** Returns true if the app can draw overlays. */
    fun canDrawOverlays(context: Context): Boolean =
        Settings.canDrawOverlays(context)

    /**
     * Launches the system settings page where the user can grant overlay permission.
     * Must be called from an Activity with a registered result launcher.
     */
    fun requestOverlayPermission(context: Context, launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        launcher.launch(intent)
    }

    /**
     * Direct intent for overlay permission (use when launcher is not available).
     */
    fun overlayPermissionIntent(context: Context): Intent =
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )

    // ── Battery optimisation ──────────────────────────────────────────────────

    /**
     * Returns true if the app is already excluded from battery optimisation.
     * If not excluded, WorkManager may be delayed or killed.
     */
    fun isBatteryOptimizationIgnored(context: Context): Boolean {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(context.packageName)
    }

    /**
     * Opens the battery optimisation settings screen.
     * Note: On some OEM devices (Xiaomi, Huawei), this may open a different screen.
     */
    fun requestBatteryOptimizationExemption(context: Context) {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = Uri.parse("package:${context.packageName}")
        }
        context.startActivity(intent)
    }

    // ── Notification (Android 13+) ────────────────────────────────────────────

    fun shouldRequestNotificationPermission(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    // ── Exact alarm (Android 12+) ─────────────────────────────────────────────

    fun canScheduleExactAlarms(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = context.getSystemService(android.app.AlarmManager::class.java)
            am.canScheduleExactAlarms()
        } else true
    }

    fun requestExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        }
    }
}
