package com.gitcommitbuddy.data.repository;

import com.gitcommitbuddy.data.api.*;
import com.gitcommitbuddy.data.db.*;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.Flow;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001e\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000e\u001a\u00020\nH\u0002J\u001c\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0013H\u0002J\u0010\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0013H\u0002J\u000e\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u001aJ\u0012\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\f0\u001aJ.\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u00132\b\b\u0002\u0010!\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\"J\u0010\u0010#\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0013H\u0002J\b\u0010$\u001a\u00020\u0013H\u0002J,\u0010%\u001a\u00020&2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000e\u001a\u00020\n2\u0006\u0010\'\u001a\u00020\u0013H\u0082@\u00a2\u0006\u0002\u0010(R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/gitcommitbuddy/data/repository/GitHubRepository;", "", "api", "Lcom/gitcommitbuddy/data/api/GitHubApiService;", "cacheDao", "Lcom/gitcommitbuddy/data/db/CommitCacheDao;", "dailyDao", "Lcom/gitcommitbuddy/data/db/DailyCommitDao;", "(Lcom/gitcommitbuddy/data/api/GitHubApiService;Lcom/gitcommitbuddy/data/db/CommitCacheDao;Lcom/gitcommitbuddy/data/db/DailyCommitDao;)V", "calculateStreakFromSearch", "", "commits", "", "Lcom/gitcommitbuddy/data/api/CommitSearchItem;", "todayCount", "fetchUserProfile", "Lcom/gitcommitbuddy/data/api/ApiResult;", "Lcom/gitcommitbuddy/data/api/GitHubUser;", "username", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isOlderThanToday", "", "utcTime", "isToday", "observeCommitStatus", "Lkotlinx/coroutines/flow/Flow;", "Lcom/gitcommitbuddy/data/db/CommitCacheEntity;", "observeRecentDays", "Lcom/gitcommitbuddy/data/db/DailyCommitEntity;", "refreshCommitStatus", "Lcom/gitcommitbuddy/data/api/CommitStatus;", "token", "commitLimit", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toLocalDateString", "todayLocalString", "updateDailyLogFromSearch", "", "todayDate", "(Ljava/util/List;ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
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
    java.lang.String token, int commitLimit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.gitcommitbuddy.data.api.ApiResult<com.gitcommitbuddy.data.api.CommitStatus>> $completion) {
        return null;
    }
    
    private final boolean isOlderThanToday(java.lang.String utcTime) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchUserProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.gitcommitbuddy.data.api.ApiResult<com.gitcommitbuddy.data.api.GitHubUser>> $completion) {
        return null;
    }
    
    private final int calculateStreakFromSearch(java.util.List<com.gitcommitbuddy.data.api.CommitSearchItem> commits, int todayCount) {
        return 0;
    }
    
    private final java.lang.Object updateDailyLogFromSearch(java.util.List<com.gitcommitbuddy.data.api.CommitSearchItem> commits, int todayCount, java.lang.String todayDate, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final boolean isToday(java.lang.String utcTime) {
        return false;
    }
    
    private final java.lang.String toLocalDateString(java.lang.String utcTime) {
        return null;
    }
    
    private final java.lang.String todayLocalString() {
        return null;
    }
}