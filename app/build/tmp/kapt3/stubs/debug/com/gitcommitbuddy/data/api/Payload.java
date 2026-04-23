package com.gitcommitbuddy.data.api;

import com.google.gson.annotations.SerializedName;

/**
 * Payload is highly polymorphic; we only map the fields we need.
 * For PushEvent: [commits] list.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BG\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0014J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0011J\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0011J\u0011\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bH\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\\\u0010 \u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0010\b\u0002\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\b2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u000bH\u00c6\u0001\u00a2\u0006\u0002\u0010!J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010%\u001a\u00020\u0005H\u00d6\u0001J\t\u0010&\u001a\u00020\u000bH\u00d6\u0001R\u001e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0006\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u0013\u0010\u0014R\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0018\u0010\f\u001a\u0004\u0018\u00010\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u001a\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0019\u0010\u0011\u00a8\u0006\'"}, d2 = {"Lcom/gitcommitbuddy/data/api/Payload;", "", "pushId", "", "size", "", "distinctSize", "commits", "", "Lcom/gitcommitbuddy/data/api/CommitRef;", "ref", "", "refType", "(Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)V", "getCommits", "()Ljava/util/List;", "getDistinctSize", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getPushId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getRef", "()Ljava/lang/String;", "getRefType", "getSize", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)Lcom/gitcommitbuddy/data/api/Payload;", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class Payload {
    @com.google.gson.annotations.SerializedName(value = "push_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long pushId = null;
    @com.google.gson.annotations.SerializedName(value = "size")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer size = null;
    @com.google.gson.annotations.SerializedName(value = "distinct_size")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer distinctSize = null;
    @com.google.gson.annotations.SerializedName(value = "commits")
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.gitcommitbuddy.data.api.CommitRef> commits = null;
    @com.google.gson.annotations.SerializedName(value = "ref")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String ref = null;
    @com.google.gson.annotations.SerializedName(value = "ref_type")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String refType = null;
    
    public Payload(@org.jetbrains.annotations.Nullable()
    java.lang.Long pushId, @org.jetbrains.annotations.Nullable()
    java.lang.Integer size, @org.jetbrains.annotations.Nullable()
    java.lang.Integer distinctSize, @org.jetbrains.annotations.Nullable()
    java.util.List<com.gitcommitbuddy.data.api.CommitRef> commits, @org.jetbrains.annotations.Nullable()
    java.lang.String ref, @org.jetbrains.annotations.Nullable()
    java.lang.String refType) {
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
    public final java.lang.Integer getDistinctSize() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.gitcommitbuddy.data.api.CommitRef> getCommits() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRef() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRefType() {
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
    public final java.lang.Integer component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.gitcommitbuddy.data.api.CommitRef> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.gitcommitbuddy.data.api.Payload copy(@org.jetbrains.annotations.Nullable()
    java.lang.Long pushId, @org.jetbrains.annotations.Nullable()
    java.lang.Integer size, @org.jetbrains.annotations.Nullable()
    java.lang.Integer distinctSize, @org.jetbrains.annotations.Nullable()
    java.util.List<com.gitcommitbuddy.data.api.CommitRef> commits, @org.jetbrains.annotations.Nullable()
    java.lang.String ref, @org.jetbrains.annotations.Nullable()
    java.lang.String refType) {
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