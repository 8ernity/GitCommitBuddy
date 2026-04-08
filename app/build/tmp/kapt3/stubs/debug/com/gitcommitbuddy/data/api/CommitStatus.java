package com.gitcommitbuddy.data.api;

import com.google.gson.annotations.SerializedName;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0019\b\u0086\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0007\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003JK\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\n\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u00032\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u001f\u001a\u00020\u0007H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\n\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\t\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000f\u00a8\u0006 "}, d2 = {"Lcom/gitcommitbuddy/data/api/CommitStatus;", "", "committedToday", "", "todayCommitCount", "", "lastCommitTime", "", "lastCommitRepo", "lastCommitMessage", "currentStreak", "(ZILjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", "getCommittedToday", "()Z", "getCurrentStreak", "()I", "getLastCommitMessage", "()Ljava/lang/String;", "getLastCommitRepo", "getLastCommitTime", "getTodayCommitCount", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class CommitStatus {
    
    /**
     * True if at least one PushEvent was found for today (UTC).
     */
    private final boolean committedToday = false;
    
    /**
     * Total number of commits found for today.
     */
    private final int todayCommitCount = 0;
    
    /**
     * ISO-8601 timestamp of the most recent commit event, or null.
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String lastCommitTime = null;
    
    /**
     * Name of the repo of the most recent commit, or null.
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String lastCommitRepo = null;
    
    /**
     * Message of the most recent commit, or null.
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String lastCommitMessage = null;
    
    /**
     * Calculated consecutive day streak (requires full event history).
     */
    private final int currentStreak = 0;
    
    public CommitStatus(boolean committedToday, int todayCommitCount, @org.jetbrains.annotations.Nullable()
    java.lang.String lastCommitTime, @org.jetbrains.annotations.Nullable()
    java.lang.String lastCommitRepo, @org.jetbrains.annotations.Nullable()
    java.lang.String lastCommitMessage, int currentStreak) {
        super();
    }
    
    /**
     * True if at least one PushEvent was found for today (UTC).
     */
    public final boolean getCommittedToday() {
        return false;
    }
    
    /**
     * Total number of commits found for today.
     */
    public final int getTodayCommitCount() {
        return 0;
    }
    
    /**
     * ISO-8601 timestamp of the most recent commit event, or null.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLastCommitTime() {
        return null;
    }
    
    /**
     * Name of the repo of the most recent commit, or null.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLastCommitRepo() {
        return null;
    }
    
    /**
     * Message of the most recent commit, or null.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLastCommitMessage() {
        return null;
    }
    
    /**
     * Calculated consecutive day streak (requires full event history).
     */
    public final int getCurrentStreak() {
        return 0;
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.data.api.CommitStatus copy(boolean committedToday, int todayCommitCount, @org.jetbrains.annotations.Nullable()
    java.lang.String lastCommitTime, @org.jetbrains.annotations.Nullable()
    java.lang.String lastCommitRepo, @org.jetbrains.annotations.Nullable()
    java.lang.String lastCommitMessage, int currentStreak) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}