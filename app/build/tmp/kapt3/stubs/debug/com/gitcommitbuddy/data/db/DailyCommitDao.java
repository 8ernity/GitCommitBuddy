package com.gitcommitbuddy.data.db;

import androidx.room.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\b0\u000bH\'J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0011\u00a8\u0006\u0012"}, d2 = {"Lcom/gitcommitbuddy/data/db/DailyCommitDao;", "", "getForDate", "Lcom/gitcommitbuddy/data/db/DailyCommitEntity;", "dateKey", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentDays", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeRecentDays", "Lkotlinx/coroutines/flow/Flow;", "pruneOlderThan", "", "cutoffDate", "upsert", "entity", "(Lcom/gitcommitbuddy/data/db/DailyCommitEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface DailyCommitDao {
    
    @androidx.room.Query(value = "SELECT * FROM daily_commits ORDER BY dateKey DESC LIMIT 90")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.gitcommitbuddy.data.db.DailyCommitEntity>> observeRecentDays();
    
    @androidx.room.Query(value = "SELECT * FROM daily_commits ORDER BY dateKey DESC LIMIT 90")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentDays(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.gitcommitbuddy.data.db.DailyCommitEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM daily_commits WHERE dateKey = :dateKey")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String dateKey, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.gitcommitbuddy.data.db.DailyCommitEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.db.DailyCommitEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM daily_commits WHERE dateKey < :cutoffDate")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object pruneOlderThan(@org.jetbrains.annotations.NotNull()
    java.lang.String cutoffDate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}