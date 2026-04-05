package com.gitcommitbuddy.data.repository;

import com.gitcommitbuddy.data.api.*;
import com.gitcommitbuddy.data.db.*;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.Flow;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0002J\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u000e\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u0015J\u0012\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\f0\u0015J$\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u001cJ\b\u0010\u001d\u001a\u00020\u0012H\u0002J\u001c\u0010\u001e\u001a\u00020\u001f2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0082@\u00a2\u0006\u0002\u0010 R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/gitcommitbuddy/data/repository/GitHubRepository;", "", "api", "Lcom/gitcommitbuddy/data/api/GitHubApiService;", "cacheDao", "Lcom/gitcommitbuddy/data/db/CommitCacheDao;", "dailyDao", "Lcom/gitcommitbuddy/data/db/DailyCommitDao;", "(Lcom/gitcommitbuddy/data/api/GitHubApiService;Lcom/gitcommitbuddy/data/db/CommitCacheDao;Lcom/gitcommitbuddy/data/db/DailyCommitDao;)V", "calculateStreak", "", "events", "", "Lcom/gitcommitbuddy/data/api/GitHubEvent;", "fetchUserProfile", "Lcom/gitcommitbuddy/data/api/ApiResult;", "Lcom/gitcommitbuddy/data/api/GitHubUser;", "username", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeCommitStatus", "Lkotlinx/coroutines/flow/Flow;", "Lcom/gitcommitbuddy/data/db/CommitCacheEntity;", "observeRecentDays", "Lcom/gitcommitbuddy/data/db/DailyCommitEntity;", "refreshCommitStatus", "Lcom/gitcommitbuddy/data/api/CommitStatus;", "token", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "todayUtcString", "updateDailyLog", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class GitHubRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.api.GitHubApiService api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.db.CommitCacheDao cacheDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.db.DailyCommitDao dailyDao = null;
    
    @javax.inject.Inject()
    public GitHubRepository(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.api.GitHubApiService api, @org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.db.CommitCacheDao cacheDao, @org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.db.DailyCommitDao dailyDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.gitcommitbuddy.data.db.CommitCacheEntity> observeCommitStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.gitcommitbuddy.data.db.DailyCommitEntity>> observeRecentDays() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refreshCommitStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.gitcommitbuddy.data.api.ApiResult<com.gitcommitbuddy.data.api.CommitStatus>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchUserProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.gitcommitbuddy.data.api.ApiResult<com.gitcommitbuddy.data.api.GitHubUser>> $completion) {
        return null;
    }
    
    private final int calculateStreak(java.util.List<com.gitcommitbuddy.data.api.GitHubEvent> events) {
        return 0;
    }
    
    private final java.lang.Object updateDailyLog(java.util.List<com.gitcommitbuddy.data.api.GitHubEvent> events, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String todayUtcString() {
        return null;
    }
}