package com.gitcommitbuddy.data.repository

import com.gitcommitbuddy.data.api.*
import com.gitcommitbuddy.data.db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
        token: String,
        commitLimit: Int = 1
    ): ApiResult<CommitStatus> = withContext(Dispatchers.IO) {
        if (username.isBlank()) {
            return@withContext ApiResult.Error("GitHub username not configured.")
        }
        try {
            val todayDate = LocalDate.now(ZoneId.systemDefault()).toString()
            
            // 1. Fetch Today's Count via Search API (Source of Truth for Today)
            val searchResponse = api.searchCommits("author:$username author-date:$todayDate")
            val todayCommitCount = if (searchResponse.isSuccessful) {
                searchResponse.body()?.totalCount ?: 0
            } else {
                0
            }

            // 2. Fetch History for Streak via Search API (Last 30 Days)
            // This ensures we get the 24-day streak even if events are filtered or missing.
            val thirtyDaysAgo = LocalDate.now(ZoneId.systemDefault()).minusDays(35).toString()
            val historyResponse = api.searchCommits("author:$username author-date:>$thirtyDaysAgo", perPage = 100)
            val allHistoryCommits = historyResponse.body()?.items ?: emptyList()

            // 3. Fetch Events for real-time repo metadata
            val eventsResponse = api.getUserEvents(username, perPage = 10)
            val pushEvents = (eventsResponse.body() ?: emptyList()).filter { it.type.equals("PushEvent", ignoreCase = true) }
            val mostRecent = pushEvents.firstOrNull()

            val status = CommitStatus(
                committedToday    = todayCommitCount >= commitLimit,
                todayCommitCount  = todayCommitCount,
                lastCommitTime    = mostRecent?.createdAt ?: allHistoryCommits.firstOrNull()?.commit?.author?.date,
                lastCommitRepo    = mostRecent?.repo?.name ?: allHistoryCommits.firstOrNull()?.repository?.name,
                lastCommitMessage = mostRecent?.payload?.commits?.firstOrNull()?.message ?: allHistoryCommits.firstOrNull()?.commit?.message,
                currentStreak     = calculateStreakFromSearch(allHistoryCommits, todayCommitCount)
            )

            cacheDao.upsert(
                CommitCacheEntity(
                    committedToday    = status.committedToday,
                    todayCommitCount  = status.todayCommitCount,
                    lastCommitTime    = status.lastCommitTime,
                    lastCommitRepo    = status.lastCommitRepo,
                    lastCommitMessage = status.lastCommitMessage,
                    currentStreak     = status.currentStreak
                )
            )
            updateDailyLogFromSearch(allHistoryCommits, todayCommitCount, todayDate)
            ApiResult.Success(status)
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    private fun isOlderThanToday(utcTime: String): Boolean {
        return try {
            val eventDate = Instant.parse(utcTime).atZone(ZoneId.systemDefault()).toLocalDate()
            val today = LocalDate.now(ZoneId.systemDefault())
            eventDate.isBefore(today)
        } catch (e: Exception) {
            false
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

    private fun calculateStreakFromSearch(commits: List<CommitSearchItem>, todayCount: Int): Int {
        val daysWithCommits = commits
            .map { it.commit.author.date.take(10) } // YYYY-MM-DD
            .toMutableSet()
        
        if (todayCount > 0) daysWithCommits.add(todayLocalString())

        val sortedDays = daysWithCommits.toSortedSet(reverseOrder())
        var streak = 0
        var currentDay = LocalDate.now(ZoneId.systemDefault())

        // If no commits today, start checking from yesterday to see if streak is alive
        if (!sortedDays.contains(currentDay.toString())) {
            currentDay = currentDay.minusDays(1)
        }

        while (sortedDays.contains(currentDay.toString())) {
            streak++
            currentDay = currentDay.minusDays(1)
        }
        return streak
    }

    private suspend fun updateDailyLogFromSearch(commits: List<CommitSearchItem>, todayCount: Int, todayDate: String) {
        val byDay = commits.groupBy { it.commit.author.date.take(10) }

        byDay.forEach { (dateKey, dayCommits) ->
            dailyDao.upsert(
                DailyCommitEntity(
                    dateKey        = dateKey,
                    didCommit      = true,
                    commitCount    = if (dateKey == todayDate) maxOf(todayCount, dayCommits.size) else dayCommits.size,
                    lastCommitTime = dayCommits.maxByOrNull { it.commit.author.date }?.commit?.author?.date
                )
            )
        }
        
        if (todayCount > 0 && !byDay.containsKey(todayDate)) {
            dailyDao.upsert(DailyCommitEntity(todayDate, true, todayCount, null))
        }

        val cutoff = LocalDate.now(ZoneId.systemDefault()).minusDays(90).toString()
        dailyDao.pruneOlderThan(cutoff)
    }

    private fun isToday(utcTime: String): Boolean {
        return try {
            val eventInstant = Instant.parse(utcTime)
            val eventDate = eventInstant.atZone(ZoneId.systemDefault()).toLocalDate()
            val today = LocalDate.now(ZoneId.systemDefault())
            eventDate == today
        } catch (e: Exception) {
            false
        }
    }

    private fun toLocalDateString(utcTime: String): String {
        return try {
            Instant.parse(utcTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString()
        } catch (e: Exception) {
            utcTime.take(10)
        }
    }

    private fun todayLocalString(): String = LocalDate.now(ZoneId.systemDefault()).toString()
}
