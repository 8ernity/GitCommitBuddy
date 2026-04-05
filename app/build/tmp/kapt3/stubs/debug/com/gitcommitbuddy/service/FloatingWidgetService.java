package com.gitcommitbuddy.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.DisplayMetrics;
import android.view.*;
import androidx.appcompat.view.ContextThemeWrapper;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;
import com.gitcommitbuddy.R;
import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.data.db.CommitCacheEntity;
import com.gitcommitbuddy.data.repository.GitHubRepository;
import com.gitcommitbuddy.databinding.LayoutFloatingBubbleBinding;
import com.gitcommitbuddy.databinding.LayoutFloatingPanelBinding;
import com.gitcommitbuddy.util.NotificationHelper;
import com.gitcommitbuddy.util.TimeFormatter;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.*;
import javax.inject.Inject;

/**
 * FloatingWidgetService
 * ─────────────────────
 * Foreground service that manages two WindowManager overlay views:
 *
 * • bubbleView  — small always-visible draggable circle
 * • panelView   — expanded info card shown on bubble tap
 */
@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 N2\u00020\u0001:\u0001NB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010%\u001a\u00020&H\u0003J\b\u0010\'\u001a\u00020&H\u0003J\b\u0010(\u001a\u00020&H\u0002J\b\u0010)\u001a\u00020&H\u0002J\b\u0010*\u001a\u00020&H\u0002J\b\u0010+\u001a\u00020&H\u0002J\u0014\u0010,\u001a\u0004\u0018\u00010-2\b\u0010.\u001a\u0004\u0018\u00010/H\u0016J\b\u00100\u001a\u00020&H\u0016J\b\u00101\u001a\u00020&H\u0016J\"\u00102\u001a\u0002032\b\u0010.\u001a\u0004\u0018\u00010/2\u0006\u00104\u001a\u0002032\u0006\u00105\u001a\u000203H\u0016J\u0010\u00106\u001a\u00020&2\u0006\u00107\u001a\u000208H\u0002J\b\u00109\u001a\u00020&H\u0002J\b\u0010:\u001a\u00020&H\u0002J\b\u0010;\u001a\u00020&H\u0002J\b\u0010<\u001a\u000203H\u0002J\u001a\u0010=\u001a\u00020&2\b\u0010>\u001a\u0004\u0018\u00010\b2\u0006\u0010?\u001a\u00020\u0006H\u0002J\b\u0010@\u001a\u00020&H\u0003J\b\u0010A\u001a\u00020&H\u0002J\b\u0010B\u001a\u00020&H\u0002J\b\u0010C\u001a\u00020&H\u0002J\u0010\u0010D\u001a\u00020&2\u0006\u0010?\u001a\u00020\u0006H\u0002J\b\u0010E\u001a\u00020&H\u0002J\u0010\u0010F\u001a\u00020&2\u0006\u0010G\u001a\u00020\nH\u0002J\u0010\u0010H\u001a\u00020&2\u0006\u0010I\u001a\u00020JH\u0002J\b\u0010K\u001a\u00020&H\u0002J\b\u0010L\u001a\u00020&H\u0002J\f\u0010M\u001a\u000203*\u000203H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0015\u001a\u00020\u00168\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001e\u0010\u001b\u001a\u00020\u001c8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006O"}, d2 = {"Lcom/gitcommitbuddy/service/FloatingWidgetService;", "Landroid/app/Service;", "()V", "bubbleBinding", "Lcom/gitcommitbuddy/databinding/LayoutFloatingBubbleBinding;", "bubbleParams", "Landroid/view/WindowManager$LayoutParams;", "bubbleView", "Landroid/view/View;", "isPanelVisible", "", "notifHelper", "Lcom/gitcommitbuddy/util/NotificationHelper;", "getNotifHelper", "()Lcom/gitcommitbuddy/util/NotificationHelper;", "setNotifHelper", "(Lcom/gitcommitbuddy/util/NotificationHelper;)V", "panelBinding", "Lcom/gitcommitbuddy/databinding/LayoutFloatingPanelBinding;", "panelParams", "panelView", "prefs", "Lcom/gitcommitbuddy/data/PreferencesManager;", "getPrefs", "()Lcom/gitcommitbuddy/data/PreferencesManager;", "setPrefs", "(Lcom/gitcommitbuddy/data/PreferencesManager;)V", "repository", "Lcom/gitcommitbuddy/data/repository/GitHubRepository;", "getRepository", "()Lcom/gitcommitbuddy/data/repository/GitHubRepository;", "setRepository", "(Lcom/gitcommitbuddy/data/repository/GitHubRepository;)V", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "windowManager", "Landroid/view/WindowManager;", "addBubble", "", "addPanel", "animateBubbleIn", "handleMarkDone", "handleRemindLater", "hidePanel", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "openUrl", "url", "", "refreshCommitStatus", "removeBubble", "removePanel", "safeScreenWidth", "safeUpdateLayout", "view", "params", "setupBubbleTouchListener", "setupPanelListeners", "showNotConfigured", "showPanel", "snapToEdge", "togglePanel", "updateBubble", "committedToday", "updatePanel", "cache", "Lcom/gitcommitbuddy/data/db/CommitCacheEntity;", "vibrateShort", "vibrateSuccess", "dpToPx", "Companion", "app_debug"})
public final class FloatingWidgetService extends android.app.Service {
    @javax.inject.Inject()
    public com.gitcommitbuddy.data.repository.GitHubRepository repository;
    @javax.inject.Inject()
    public com.gitcommitbuddy.data.PreferencesManager prefs;
    @javax.inject.Inject()
    public com.gitcommitbuddy.util.NotificationHelper notifHelper;
    private android.view.WindowManager windowManager;
    @org.jetbrains.annotations.Nullable()
    private android.view.View bubbleView;
    @org.jetbrains.annotations.Nullable()
    private android.view.View panelView;
    private com.gitcommitbuddy.databinding.LayoutFloatingBubbleBinding bubbleBinding;
    private com.gitcommitbuddy.databinding.LayoutFloatingPanelBinding panelBinding;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.Nullable()
    private android.view.WindowManager.LayoutParams bubbleParams;
    @org.jetbrains.annotations.Nullable()
    private android.view.WindowManager.LayoutParams panelParams;
    private boolean isPanelVisible = false;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_REFRESH = "com.gitcommitbuddy.action.REFRESH";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "com.gitcommitbuddy.action.STOP";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_MARK_DONE = "com.gitcommitbuddy.action.MARK_DONE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_REMIND_LATER = "com.gitcommitbuddy.action.REMIND_LATER";
    private static final float DRAG_SLOP = 12.0F;
    @org.jetbrains.annotations.NotNull()
    public static final com.gitcommitbuddy.service.FloatingWidgetService.Companion Companion = null;
    
    public FloatingWidgetService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.data.repository.GitHubRepository getRepository() {
        return null;
    }
    
    public final void setRepository(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.repository.GitHubRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.data.PreferencesManager getPrefs() {
        return null;
    }
    
    public final void setPrefs(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.PreferencesManager p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.util.NotificationHelper getNotifHelper() {
        return null;
    }
    
    public final void setNotifHelper(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.util.NotificationHelper p0) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @android.annotation.SuppressLint(value = {"InflateParams"})
    private final void addBubble() {
    }
    
    private final void animateBubbleIn() {
    }
    
    @android.annotation.SuppressLint(value = {"ClickableViewAccessibility"})
    private final void setupBubbleTouchListener() {
    }
    
    private final void snapToEdge(android.view.WindowManager.LayoutParams params) {
    }
    
    @android.annotation.SuppressLint(value = {"InflateParams"})
    private final void addPanel() {
    }
    
    private final void setupPanelListeners() {
    }
    
    private final void handleRemindLater() {
    }
    
    private final void handleMarkDone() {
    }
    
    private final void togglePanel() {
    }
    
    private final void showPanel() {
    }
    
    private final void hidePanel() {
    }
    
    private final void refreshCommitStatus() {
    }
    
    private final void updateBubble(boolean committedToday) {
    }
    
    private final void updatePanel(com.gitcommitbuddy.data.db.CommitCacheEntity cache) {
    }
    
    private final void showNotConfigured() {
    }
    
    private final void safeUpdateLayout(android.view.View view, android.view.WindowManager.LayoutParams params) {
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    private final int safeScreenWidth() {
        return 0;
    }
    
    private final void removeBubble() {
    }
    
    private final void removePanel() {
    }
    
    private final void openUrl(java.lang.String url) {
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    private final void vibrateShort() {
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    private final void vibrateSuccess() {
    }
    
    private final int dpToPx(int $this$dpToPx) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/gitcommitbuddy/service/FloatingWidgetService$Companion;", "", "()V", "ACTION_MARK_DONE", "", "ACTION_REFRESH", "ACTION_REMIND_LATER", "ACTION_STOP", "DRAG_SLOP", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}