package com.gitcommitbuddy.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.gitcommitbuddy.R;
import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.databinding.ActivityMainBinding;
import com.gitcommitbuddy.service.FloatingWidgetService;
import com.gitcommitbuddy.service.ReminderWorker;
import com.gitcommitbuddy.ui.settings.SettingsActivity;
import com.gitcommitbuddy.util.MotivationalMessages;
import com.gitcommitbuddy.util.NotificationHelper;
import com.gitcommitbuddy.util.PermissionHelper;
import com.gitcommitbuddy.util.TimeFormatter;
import com.gitcommitbuddy.viewmodel.CommitUiState;
import com.gitcommitbuddy.viewmodel.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u001eH\u0002J\u0012\u0010 \u001a\u00020\u001e2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u0016J\u0010\u0010\'\u001a\u00020$2\u0006\u0010(\u001a\u00020)H\u0016J\b\u0010*\u001a\u00020\u001eH\u0014J\u0010\u0010+\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020\rH\u0002J\b\u0010-\u001a\u00020\u001eH\u0002J\b\u0010.\u001a\u00020\u001eH\u0002J\b\u0010/\u001a\u00020\u001eH\u0002J\b\u00100\u001a\u00020\u001eH\u0002J\b\u00101\u001a\u00020\u001eH\u0002J\u0010\u00102\u001a\u00020\u001e2\u0006\u00103\u001a\u00020\rH\u0002J\b\u00104\u001a\u00020\u001eH\u0002J\b\u00105\u001a\u00020\u001eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001c\u0010\u000b\u001a\u0010\u0012\f\u0012\n \u000e*\u0004\u0018\u00010\r0\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u000f\u001a\u0010\u0012\f\u0012\n \u000e*\u0004\u0018\u00010\u00100\u00100\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u00020\u00128\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001b\u0010\u0017\u001a\u00020\u00188BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u0019\u0010\u001a\u00a8\u00066"}, d2 = {"Lcom/gitcommitbuddy/ui/main/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/gitcommitbuddy/databinding/ActivityMainBinding;", "notifHelper", "Lcom/gitcommitbuddy/util/NotificationHelper;", "getNotifHelper", "()Lcom/gitcommitbuddy/util/NotificationHelper;", "setNotifHelper", "(Lcom/gitcommitbuddy/util/NotificationHelper;)V", "notifPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "kotlin.jvm.PlatformType", "overlayPermissionLauncher", "Landroid/content/Intent;", "prefs", "Lcom/gitcommitbuddy/data/PreferencesManager;", "getPrefs", "()Lcom/gitcommitbuddy/data/PreferencesManager;", "setPrefs", "(Lcom/gitcommitbuddy/data/PreferencesManager;)V", "viewModel", "Lcom/gitcommitbuddy/viewmodel/MainViewModel;", "getViewModel", "()Lcom/gitcommitbuddy/viewmodel/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "checkOverlayAndStartWidget", "", "observeViewModel", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onResume", "openUrl", "url", "requestPermissionsIfNeeded", "scheduleReminders", "setupUI", "showBatteryOptimizationDialog", "showOverlayPermissionDialog", "showSnackbar", "msg", "startFloatingWidget", "stopFloatingWidget", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.gitcommitbuddy.databinding.ActivityMainBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @javax.inject.Inject()
    public com.gitcommitbuddy.util.NotificationHelper notifHelper;
    @javax.inject.Inject()
    public com.gitcommitbuddy.data.PreferencesManager prefs;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> overlayPermissionLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> notifPermissionLauncher = null;
    
    public MainActivity() {
        super();
    }
    
    private final com.gitcommitbuddy.viewmodel.MainViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.util.NotificationHelper getNotifHelper() {
        return null;
    }
    
    public final void setNotifHelper(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.util.NotificationHelper p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.data.PreferencesManager getPrefs() {
        return null;
    }
    
    public final void setPrefs(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.PreferencesManager p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void setupUI() {
    }
    
    private final void observeViewModel() {
    }
    
    private final void requestPermissionsIfNeeded() {
    }
    
    private final void scheduleReminders() {
    }
    
    private final void checkOverlayAndStartWidget() {
    }
    
    private final void showOverlayPermissionDialog() {
    }
    
    private final void showBatteryOptimizationDialog() {
    }
    
    private final void startFloatingWidget() {
    }
    
    private final void stopFloatingWidget() {
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final void openUrl(java.lang.String url) {
    }
    
    private final void showSnackbar(java.lang.String msg) {
    }
}