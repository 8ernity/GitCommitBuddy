package com.gitcommitbuddy.data.api;

import com.google.gson.annotations.SerializedName;

/**
 * Payload is highly polymorphic; we only map the fields we need.
 * For PushEvent: [commits] list.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B)\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u000e\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0010J\u0011\u0010\u0014\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007H\u00c6\u0003J8\u0010\u0015\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0010\b\u0002\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001R\u001e\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\f\u0010\rR\u001a\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001d"}, d2 = {"Lcom/gitcommitbuddy/data/api/Payload;", "", "pushId", "", "size", "", "commits", "", "Lcom/gitcommitbuddy/data/api/CommitRef;", "(Ljava/lang/Long;Ljava/lang/Integer;Ljava/util/List;)V", "getCommits", "()Ljava/util/List;", "getPushId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getSize", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "copy", "(Ljava/lang/Long;Ljava/lang/Integer;Ljava/util/List;)Lcom/gitcommitbuddy/data/api/Payload;", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class Payload {
    @com.google.gson.annotations.SerializedName(value = "push_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long pushId = null;
    @com.google.gson.annotations.SerializedName(value = "size")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer size = null;
    @com.google.gson.annotations.SerializedName(value = "commits")
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.gitcommitbuddy.data.api.CommitRef> commits = null;
    
    public Payload(@org.jetbrains.annotations.Nullable()
    java.lang.Long pushId, @org.jetbrains.annotations.Nullable()
    java.lang.Integer size, @org.jetbrains.annotations.Nullable()
    java.util.List<com.gitcommitbuddy.data.api.CommitRef> commits) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getPushId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getSize() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.gitcommitbuddy.data.api.CommitRef> getCommits() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.gitcommitbuddy.data.api.CommitRef> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.data.api.Payload copy(@org.jetbrains.annotations.Nullable()
    java.lang.Long pushId, @org.jetbrains.annotations.Nullable()
    java.lang.Integer size, @org.jetbrains.annotations.Nullable()
    java.util.List<com.gitcommitbuddy.data.api.CommitRef> commits) {
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