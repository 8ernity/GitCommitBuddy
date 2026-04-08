package com.gitcommitbuddy.viewmodel;

import androidx.lifecycle.ViewModel;
import com.gitcommitbuddy.data.PreferencesManager;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\fJ\u0016\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0014J\u000e\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\fJ\u000e\u0010%\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020\u0007J\u000e\u0010\'\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020\u0007J\u000e\u0010(\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020\u0007R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000eR\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000eR\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u000eR\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u000eR\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u000e\u00a8\u0006)"}, d2 = {"Lcom/gitcommitbuddy/viewmodel/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "prefs", "Lcom/gitcommitbuddy/data/PreferencesManager;", "(Lcom/gitcommitbuddy/data/PreferencesManager;)V", "_darkMode", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_notifEnabled", "_widgetEnabled", "bubbleColor", "Lkotlinx/coroutines/flow/StateFlow;", "", "getBubbleColor", "()Lkotlinx/coroutines/flow/StateFlow;", "darkMode", "getDarkMode", "notifEnabled", "getNotifEnabled", "reminderHour", "", "getReminderHour", "reminderMinute", "getReminderMinute", "token", "getToken", "username", "getUsername", "widgetEnabled", "getWidgetEnabled", "saveCredentials", "", "saveReminderTime", "hour", "minute", "setBubbleColor", "color", "setDarkMode", "enabled", "setNotificationsEnabled", "setWidgetEnabled", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.PreferencesManager prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _widgetEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> widgetEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _notifEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> notifEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _darkMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> darkMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> username = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> token = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> reminderHour = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> reminderMinute = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> bubbleColor = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.PreferencesManager prefs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getWidgetEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getNotifEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getDarkMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getUsername() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getToken() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getReminderHour() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getReminderMinute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getBubbleColor() {
        return null;
    }
    
    public final void saveCredentials(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
    
    public final void saveReminderTime(int hour, int minute) {
    }
    
    public final void setWidgetEnabled(boolean enabled) {
    }
    
    public final void setNotificationsEnabled(boolean enabled) {
    }
    
    public final void setDarkMode(boolean enabled) {
    }
    
    public final void setBubbleColor(@org.jetbrains.annotations.NotNull()
    java.lang.String color) {
    }
}