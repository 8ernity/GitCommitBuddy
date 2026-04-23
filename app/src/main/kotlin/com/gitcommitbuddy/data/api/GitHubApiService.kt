package com.gitcommitbuddy.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for GitHub REST API v3.
 *
 * Base URL: https://api.github.com/
 *
 * Authentication: pass a Personal Access Token (PAT) as
 *   Authorization: Bearer <token>
 *
 * GitHub rate limits:
 *   - Authenticated:   5 000 req/hour
 *   - Unauthenticated:    60 req/hour
 *
 * ⚠️ TOKEN NOTE: The token is injected via the OkHttp interceptor
 *    in [NetworkModule], NOT hard-coded here. Users enter it in Settings.
 */
interface GitHubApiService {

    /**
     * Fetch the user's events.
     * Returns up to 300 events (paginated, max 100/page).
     *
     * Note: If authenticated with a PAT that has 'repo' scope, this will
     * include private events.
     *
     * @param username  GitHub username
     * @param perPage   Items per page (1-100)
     * @param page      Page number (1-based)
     */
    @GET("users/{username}/events")
    suspend fun getUserEvents(
        @Path("username") username: String,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): Response<List<GitHubEvent>>

    /**
     * Search for commits by author and date.
     * This is more accurate for private repos as it uses the Search API.
     * Query example: author:8ernity author-date:2024-05-20
     */
    @GET("search/commits")
    suspend fun searchCommits(
        @Query("q") query: String,
        @Query("sort") sort: String = "author-date",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 1
    ): Response<SearchCommitsResponse>

    /**
     * Fetch user profile information.
     */
    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<GitHubUser>

    /**
     * Fetch the currently authenticated user's profile.
     * Requires a valid token with `read:user` scope.
     */
    @GET("user")
    suspend fun getAuthenticatedUser(): Response<GitHubUser>
}
