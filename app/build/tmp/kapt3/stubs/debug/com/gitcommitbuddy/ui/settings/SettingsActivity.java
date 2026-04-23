package com.gitcommitbuddy.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.gitcommitbuddy.R;
import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.databinding.ActivitySettingsBinding;
import com.gitcommitbuddy.service.FloatingWidgetService;
import com.gitcommitbuddy.service.ReminderWorker;
import com.gitcommitbuddy.util.NotificationHelper;
import com.gitcommitbuddy.util.PermissionHelper;
import com.gitcommitbuddy.util.TimeFormatter;
import com.gitcommitbuddy.viewmodel.SettingsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0013H\u0002J\b\u0010 \u001a\u00020\u001eH\u0002J\b\u0010!\u001a\u00020\u001eH\u0002J\u0012\u0010\"\u001a\u00020\u001e2\b\u0010#\u001a\u0004\u0018\u00010$H\u0014J\u0010\u0010%\u001a\u00020\u00132\u0006\u0010&\u001a\u00020\'H\u0016J\u0010\u0010(\u001a\u00020\u00132\u0006\u0010)\u001a\u00020*H\u0016J\b\u0010+\u001a\u00020\u001eH\u0002J\u0010\u0010,\u001a\u00020\u001e2\u0006\u0010-\u001a\u00020.H\u0002J\b\u0010/\u001a\u00020\u001eH\u0002J\b\u00100\u001a\u00020\u001eH\u0002J\b\u00101\u001a\u00020\u001eH\u0002J\b\u00102\u001a\u00020\u001eH\u0002J\u0018\u00103\u001a\u00020\u001e2\u0006\u00104\u001a\u00020\u00062\u0006\u00105\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001c\u0010\u000e\u001a\u0010\u0012\f\u0012\n \u0011*\u0004\u0018\u00010\u00100\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0017\u001a\u00020\u00188BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u0019\u0010\u001a\u00a8\u00066"}, d2 = {"Lcom/gitcommitbuddy/ui/settings/SettingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/gitcommitbuddy/databinding/ActivitySettingsBinding;", "currentReminderHour", "", "currentReminderMinute", "notifHelper", "Lcom/gitcommitbuddy/util/NotificationHelper;", "getNotifHelper", "()Lcom/gitcommitbuddy/util/NotificationHelper;", "setNotifHelper", "(Lcom/gitcommitbuddy/util/NotificationHelper;)V", "overlayPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "programmaticDarkMode", "", "programmaticNotif", "suppressWidgetSwitch", "tokenVisible", "viewModel", "Lcom/gitcommitbuddy/viewmodel/SettingsViewModel;", "getViewModel", "()Lcom/gitcommitbuddy/viewmodel/SettingsViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "applyDarkMode", "", "enabled", "checkOverlayAndStartWidget", "observeViewModel", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "setupUI", "showSnackbar", "msg", "", "showTimePicker", "showTokenHelp", "startFloatingService", "stopFloatingService", "updateReminderLabel", "hour", "minute", "app_debug"})
public final class SettingsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.gitcommitbuddy.databinding.ActivitySettingsBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @javax.inject.Inject()
    public com.gitcommitbuddy.util.NotificationHelper notifHelper;
    private boolean tokenVisible = false;
    private int currentReminderHour = 21;
    private int currentReminderMinute = 0;
    private boolean suppressWidgetSwitch = false;
    private boolean programmaticDarkMode = false;
    private boolean programmaticNotif = false;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> overlayPermissionLauncher = null;
    
    public SettingsActivity() {
        super();
    }
    
    private final com.gitcommitbuddy.viewmodel.SettingsViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.util.NotificationHelper getNotifHelper() {
        return null;
    }
    
    public final void setNotifHelper(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.util.NotificationHelper p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupUI() {
    }
    
    private final void observeViewModel() {
    }
    
    /**
     * Checks overlay permission before starting the service.
     * If not granted, shows a rationale dialog and launches the system settings page.
     */
    private final void checkOverlayAndStartWidget() {
    }
    
    private final void startFloatingService() {
    }
    
    private final void stopFloatingService() {
    }
    
    private final void showTimePicker() {
    }
    
    private final void updateReminderLabel(int hour, int minute) {
    }
    
    private final void applyDarkMode(boolean enabled) {
    }
    
    private final void showTokenHelp() {
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
    
    private final void showSnackbar(java.lang.String msg) {
    }
}