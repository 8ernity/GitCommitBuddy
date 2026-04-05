package com.gitcommitbuddy.data.api;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for GitHub REST API v3.
 *
 * Base URL: https://api.github.com/
 *
 * Authentication: pass a Personal Access Token (PAT) as
 *  Authorization: Bearer <token>
 *
 * GitHub rate limits:
 *  - Authenticated:   5 000 req/hour
 *  - Unauthenticated:    60 req/hour
 *
 * ⚠️ TOKEN NOTE: The token is injected via the OkHttp interceptor
 *   in [NetworkModule], NOT hard-coded here. Users enter it in Settings.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u001e\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ8\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u00032\b\b\u0001\u0010\u0007\u001a\u00020\b2\b\b\u0003\u0010\r\u001a\u00020\u000e2\b\b\u0003\u0010\u000f\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/gitcommitbuddy/data/api/GitHubApiService;", "", "getAuthenticatedUser", "Lretrofit2/Response;", "Lcom/gitcommitbuddy/data/api/GitHubUser;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUser", "username", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserEvents", "", "Lcom/gitcommitbuddy/data/api/GitHubEvent;", "perPage", "", "page", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface GitHubApiService {
    
    /**
     * Fetch the authenticated user's public events.
     * Returns up to 300 events (paginated, max 100/page).
     *
     * @param username  GitHub username
     * @param perPage   Items per page (1-100)
     * @param page      Page number (1-based)
     */
    @retrofit2.http.GET(value = "users/{username}/events/public")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUserEvents(@retrofit2.http.Path(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "per_page")
    int perPage, @retrofit2.http.Query(value = "page")
    int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.gitcommitbuddy.data.api.GitHubEvent>>> $completion);
    
    /**
     * Fetch user profile information.
     */
    @retrofit2.http.GET(value = "users/{username}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUser(@retrofit2.http.Path(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.gitcommitbuddy.data.api.GitHubUser>> $completion);
    
    /**
     * Fetch the currently authenticated user's profile.
     * Requires a valid token with `read:user` scope.
     */
    @retrofit2.http.GET(value = "user")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAuthenticatedUser(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.gitcommitbuddy.data.api.GitHubUser>> $completion);
    
    /**
     * Retrofit interface for GitHub REST API v3.
     *
     * Base URL: https://api.github.com/
     *
     * Authentication: pass a Personal Access Token (PAT) as
     *  Authorization: Bearer <token>
     *
     * GitHub rate limits:
     *  - Authenticated:   5 000 req/hour
     *  - Unauthenticated:    60 req/hour
     *
     * ⚠️ TOKEN NOTE: The token is injected via the OkHttp interceptor
     *   in [NetworkModule], NOT hard-coded here. Users enter it in Settings.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}