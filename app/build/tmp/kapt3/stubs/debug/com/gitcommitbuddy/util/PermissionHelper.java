package com.gitcommitbuddy.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.activity.result.ActivityResultLauncher;

/**
 * Centralises all permission-checking logic.
 * Keeps Activities clean by moving this boilerplate here.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0006J\u001c\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u0010J\u0006\u0010\u0011\u001a\u00020\u0004\u00a8\u0006\u0012"}, d2 = {"Lcom/gitcommitbuddy/util/PermissionHelper;", "", "()V", "canDrawOverlays", "", "context", "Landroid/content/Context;", "canScheduleExactAlarms", "isBatteryOptimizationIgnored", "overlayPermissionIntent", "Landroid/content/Intent;", "requestBatteryOptimizationExemption", "", "requestExactAlarmPermission", "requestOverlayPermission", "launcher", "Landroidx/activity/result/ActivityResultLauncher;", "shouldRequestNotificationPermission", "app_debug"})
public final class PermissionHelper {
    @org.jetbrains.annotations.NotNull()
    public static final com.gitcommitbuddy.util.PermissionHelper INSTANCE = null;
    
    private PermissionHelper() {
        super();
    }
    
    /**
     * Returns true if the app can draw overlays.
     */
    public final boolean canDrawOverlays(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    /**
     * Launches the system settings page where the user can grant overlay permission.
     * Must be called from an Activity with a registered result launcher.
     */
    public final void requestOverlayPermission(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    androidx.activity.result.ActivityResultLauncher<android.content.Intent> launcher) {
    }
    
    /**
     * Direct intent for overlay permission (use when launcher is not available).
     */
    @org.jetbrains.annotations.NotNull()
    public final android.content.Intent overlayPermissionIntent(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * Returns true if the app is already excluded from battery optimisation.
     * If not excluded, WorkManager may be delayed or killed.
     */
    public final boolean isBatteryOptimizationIgnored(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    /**
     * Opens the battery optimisation settings screen.
     * Note: On some OEM devices (Xiaomi, Huawei), this may open a different screen.
     */
    public final void requestBatteryOptimizationExemption(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final boolean shouldRequestNotificationPermission() {
        return false;
    }
    
    public final boolean canScheduleExactAlarms(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final void requestExactAlarmPermission(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}