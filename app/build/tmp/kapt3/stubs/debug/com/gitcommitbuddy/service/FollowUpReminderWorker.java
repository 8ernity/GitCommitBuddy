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
 * Fires a stronger missed-commit alert if the user still hasn't committed
 * ~2 hours after the daily reminder.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B3\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u000fR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/gitcommitbuddy/service/FollowUpReminderWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "workerParams", "Landroidx/work/WorkerParameters;", "repository", "Lcom/gitcommitbuddy/data/repository/GitHubRepository;", "prefs", "Lcom/gitcommitbuddy/data/PreferencesManager;", "notifHelper", "Lcom/gitcommitbuddy/util/NotificationHelper;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;Lcom/gitcommitbuddy/data/repository/GitHubRepository;Lcom/gitcommitbuddy/data/PreferencesManager;Lcom/gitcommitbuddy/util/NotificationHelper;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.hilt.work.HiltWorker()
public final class FollowUpReminderWorker extends androidx.work.CoroutineWorker {
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.repository.GitHubRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.PreferencesManager prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.util.NotificationHelper notifHelper = null;
    
    @dagger.assisted.AssistedInject()
    public FollowUpReminderWorker(@dagger.assisted.Assisted()
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
}