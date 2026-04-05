package com.gitcommitbuddy.service;

import android.content.Context;
import androidx.hilt.work.HiltWorker;
import androidx.work.*;
import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.data.api.ApiResult;
import com.gitcommitbuddy.data.repository.GitHubRepository;
import com.gitcommitbuddy.util.NotificationHelper;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * ReminderWorker — daily commit reminder via WorkManager.
 *
 * Workers:
 * • ReminderWorker         — periodic, once per day at user-configured time
 * • FollowUpReminderWorker — one-shot, fires ~2h after daily if not committed
 * • RemindLaterWorker      — one-shot, fires on user tapping "Remind Me Later"
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B3\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u000fR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/gitcommitbuddy/service/ReminderWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "workerParams", "Landroidx/work/WorkerParameters;", "repository", "Lcom/gitcommitbuddy/data/repository/GitHubRepository;", "prefs", "Lcom/gitcommitbuddy/data/PreferencesManager;", "notifHelper", "Lcom/gitcommitbuddy/util/NotificationHelper;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;Lcom/gitcommitbuddy/data/repository/GitHubRepository;Lcom/gitcommitbuddy/data/PreferencesManager;Lcom/gitcommitbuddy/util/NotificationHelper;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
@androidx.hilt.work.HiltWorker()
public final class ReminderWorker extends androidx.work.CoroutineWorker {
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.repository.GitHubRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.PreferencesManager prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.util.NotificationHelper notifHelper = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String WORK_NAME_DAILY = "daily_reminder";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String WORK_NAME_FOLLOWUP = "followup_reminder";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String WORK_NAME_REMIND_LATER = "remind_later";
    @org.jetbrains.annotations.NotNull()
    public static final com.gitcommitbuddy.service.ReminderWorker.Companion Companion = null;
    
    @dagger.assisted.AssistedInject()
    public ReminderWorker(@dagger.assisted.Assisted()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @dagger.assisted.Assisted()
    @org.jetbrains.annotations.NotNull()
    androidx.work.WorkerParameters workerParams, @org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.repository.GitHubRepository repository, @org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.PreferencesManager prefs, @org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.util.NotificationHelper notifHelper) {
        super(null, null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object doWork(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u001e\u0010\u0011\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\nJ\u0018\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\bJ\u0018\u0010\u0016\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/gitcommitbuddy/service/ReminderWorker$Companion;", "", "()V", "WORK_NAME_DAILY", "", "WORK_NAME_FOLLOWUP", "WORK_NAME_REMIND_LATER", "calculateInitialDelay", "", "hour", "", "minute", "cancelAll", "", "context", "Landroid/content/Context;", "cancelFollowUp", "scheduleDailyReminder", "reminderHour", "reminderMinute", "scheduleFollowUpReminder", "delayMinutes", "scheduleRemindLaterNotification", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void scheduleDailyReminder(@org.jetbrains.annotations.NotNull()
        android.content.Context context, int reminderHour, int reminderMinute) {
        }
        
        public final void scheduleFollowUpReminder(@org.jetbrains.annotations.NotNull()
        android.content.Context context, long delayMinutes) {
        }
        
        /**
         * "Remind Me Later" — fires a plain notification after [delayMinutes].
         * No network constraint: notification fires even offline.
         */
        public final void scheduleRemindLaterNotification(@org.jetbrains.annotations.NotNull()
        android.content.Context context, long delayMinutes) {
        }
        
        /**
         * Cancel all scheduled workers.
         */
        public final void cancelAll(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        /**
         * Cancel only the follow-up / remind-later workers (used by "Mark as Done").
         */
        public final void cancelFollowUp(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        /**
         * Returns milliseconds until the next occurrence of [hour]:[minute].
         * If that time has already passed today, targets tomorrow.
         */
        public final long calculateInitialDelay(int hour, int minute) {
            return 0L;
        }
    }
}