package com.gitcommitbuddy.data.repository

import com.gitcommitbuddy.data.api.*
import com.gitcommitbuddy.data.db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepository @Inject constructor(
    private val api: GitHubApiService,
    private val cacheDao: CommitCacheDao,
    private val dailyDao: DailyCommitDao
) {

    fun observeCommitStatus(): Flow<CommitCacheEntity?> = cacheDao.observeCache()
    fun observeRecentDays(): Flow<List<DailyCommitEntity>> = dailyDao.observeRecentDays()

    suspend fun refreshCommitStatus(
        username: String,
        token: String
    ): ApiResult<CommitStatus> = withContext(Dispatchers.IO) {
        if (username.isBlank()) {
            return@withContext ApiResult.Error("GitHub username not configured.")
        }
        try {
            val response = api.getUserEvents(username, perPage = 100, page = 1)
            if (!response.isSuccessful) {
                val msg = when (response.code()) {
                    401  -> "Invalid GitHub token. Please update in Settings."
                    403  -> "API rate limit exceeded. Try again later."
                    404  -> "GitHub user '$username' not found."
                    else -> "GitHub API error: HTTP ${response.code()}"
                }
                return@withContext ApiResult.Error(msg, response.code())
            }

            val events     = response.body() ?: emptyList()
            val pushEvents = events.filter { it.type == "PushEvent" }
            val todayUtc   = todayUtcString()
            val todayPushes = pushEvents.filter { it.createdAt.startsWith(todayUtc) }
            val mostRecent  = pushEvents.firstOrNull()

            val status = CommitStatus(
                committedToday    = todayPushes.isNotEmpty(),
                lastCommitTime    = mostRecent?.createdAt,
                lastCommitRepo    = mostRecent?.repo?.name,
                lastCommitMessage = mostRecent?.payload?.commits?.firstOrNull()?.message,
                currentStreak     = calculateStreak(pushEvents)
            )

            cacheDao.upsert(
                CommitCacheEntity(
                    committedToday    = status.committedToday,
                    lastCommitTime    = status.lastCommitTime,
                    lastCommitRepo    = status.lastCommitRepo,
                    lastCommitMessage = status.lastCommitMessage,
                    currentStreak     = status.currentStreak
                )
            )
            updateDailyLog(pushEvents)
            ApiResult.Success(status)
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    suspend fun fetchUserProfile(username: String): ApiResult<GitHubUser> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getUser(username)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) ApiResult.Success(body)
                    else ApiResult.Error("Empty response body")
                } else {
                    ApiResult.Error("HTTP ${response.code()}", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.localizedMessage ?: "Network error")
            }
        }

    private fun calculateStreak(events: List<GitHubEvent>): Int {
        val daysWithCommits = events
            .filter { it.type == "PushEvent" }
            .map { it.createdAt.substring(0, 10) }
            .toSortedSet(reverseOrder())

        val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        var streak = 0
        var day = dateFmt.format(cal.time)

        while (daysWithCommits.contains(day)) {
            streak++
            cal.add(Calendar.DAY_OF_YEAR, -1)
            day = dateFmt.format(cal.time)
        }
        return streak
    }

    private suspend fun updateDailyLog(events: List<GitHubEvent>) {
        val byDay = events.filter { it.type == "PushEvent" }
            .groupBy { it.createdAt.substring(0, 10) }

        byDay.forEach { (dateKey, dayEvents) ->
            dailyDao.upsert(
                DailyCommitEntity(
                    dateKey        = dateKey,
                    didCommit      = true,
                    commitCount    = dayEvents.sumOf { it.payload?.size ?: 0 },
                    lastCommitTime = dayEvents.maxByOrNull { it.createdAt }?.createdAt
                )
            )
        }

        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.add(Calendar.DAY_OF_YEAR, -90)
        val cutoff = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(cal.time)
        dailyDao.pruneOlderThan(cutoff)
    }

    private fun todayUtcString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date())
}
