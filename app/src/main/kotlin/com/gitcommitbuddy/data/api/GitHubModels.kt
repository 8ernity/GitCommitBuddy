package com.gitcommitbuddy.data.api

import com.google.gson.annotations.SerializedName

// ─────────────────────────────────────────────────────────────────────────────
// GitHub REST API response models
// Docs: https://docs.github.com/en/rest/activity/events
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Represents a single GitHub event (push, PR, etc.)
 * We primarily care about [PushEvent] types to detect commits.
 */
data class GitHubEvent(
    @SerializedName("id")          val id: String,
    @SerializedName("type")        val type: String,          // "PushEvent", "PullRequestEvent", …
    @SerializedName("actor")       val actor: Actor,
    @SerializedName("repo")        val repo: Repo,
    @SerializedName("payload")     val payload: Payload?,
    @SerializedName("created_at") val createdAt: String       // ISO-8601 e.g. "2024-01-15T21:00:00Z"
)

data class Actor(
    @SerializedName("id")           val id: Long,
    @SerializedName("login")        val login: String,
    @SerializedName("display_login") val displayLogin: String?,
    @SerializedName("avatar_url")   val avatarUrl: String
)

data class Repo(
    @SerializedName("id")   val id: Long,
    @SerializedName("name") val name: String,   // "owner/repo"
    @SerializedName("url")  val url: String
)

/**
 * Payload is highly polymorphic; we only map the fields we need.
 * For PushEvent: [commits] list.
 */
data class Payload(
    @SerializedName("push_id")  val pushId: Long?,
    @SerializedName("size")     val size: Int?,        // number of commits in push
    @SerializedName("commits")  val commits: List<CommitRef>?
)

data class CommitRef(
    @SerializedName("sha")     val sha: String,
    @SerializedName("message") val message: String,
    @SerializedName("author")  val author: CommitAuthor
)

data class CommitAuthor(
    @SerializedName("email") val email: String,
    @SerializedName("name")  val name: String
)

// ─────────────────────────────────────────────────────────────────────────────
// GitHub User profile
// ─────────────────────────────────────────────────────────────────────────────

data class GitHubUser(
    @SerializedName("login")       val login: String,
    @SerializedName("id")          val id: Long,
    @SerializedName("avatar_url")  val avatarUrl: String,
    @SerializedName("name")        val name: String?,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("followers")   val followers: Int,
    @SerializedName("following")   val following: Int,
    @SerializedName("created_at") val createdAt: String
)

// ─────────────────────────────────────────────────────────────────────────────
// Internal domain model — result of processing events
// ─────────────────────────────────────────────────────────────────────────────

data class CommitStatus(
    /** True if at least one PushEvent was found for today (UTC). */
    val committedToday: Boolean,
    /** ISO-8601 timestamp of the most recent commit event, or null. */
    val lastCommitTime: String?,
    /** Name of the repo of the most recent commit, or null. */
    val lastCommitRepo: String?,
    /** Message of the most recent commit, or null. */
    val lastCommitMessage: String?,
    /** Calculated consecutive day streak (requires full event history). */
    val currentStreak: Int = 0
)
