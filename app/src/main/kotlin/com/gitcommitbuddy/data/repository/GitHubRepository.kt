package com.gitcommitbuddy.data.repository

import com.gitcommitbuddy.data.PreferencesManager
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
    private val dailyDao: DailyCommitDao,
    private val prefs: PreferencesManager
) {

    fun observeCommitStatus(): Flow<CommitCacheEntity?> = cacheDao.observeCache()
    fun observeRecentDays(): Flow<List<DailyCommitEntity>> = dailyDao.observeRecentDays()

    suspend fun refreshCommitStatus(
        username: String,
        token: String,
        commitLimit: Int = 1
    ): ApiResult<CommitStatus> = withContext(Dispatchers.IO) {
        var resolvedUsername = username.trim()
        if (resolvedUsername.isBlank()) {
            return@withContext ApiResult.Error("GitHub username not configured.")
        }

        try {
            // 1. Resolve real username from token if possible
            if (token.isNotBlank()) {
                val userResponse = api.getAuthenticatedUser()
                if (userResponse.isSuccessful) {
                    val login = userResponse.body()?.login
                    if (!login.isNullOrBlank() && login != resolvedUsername) {
                        resolvedUsername = login
                        prefs.saveGitHubCredentials(resolvedUsername, token)
                    }
                }
            }

            val todayDate = LocalDate.now(ZoneId.systemDefault()).toString()
            val yesterdayDate = LocalDate.now(ZoneId.systemDefault()).minusDays(1).toString()
            
            // 2. Fetch commits from Search API (Covers Yesterday + Today to handle timezones)
            val searchResponse = api.searchCommits("author:$resolvedUsername author-date:>=$yesterdayDate", perPage = 100)
            val searchItems = searchResponse.body()?.items ?: emptyList()
            
            // 3. Fetch Events for real-time metadata
            val eventsResponse = api.getUserEvents(resolvedUsername, perPage = 50)
            val allEvents = eventsResponse.body() ?: emptyList()
            val pushEvents = allEvents.filter { it.type.equals("PushEvent", ignoreCase = true) }

            // 4. Robust Counting: Use unique SHAs to avoid double-counting
            val uniqueCommitShas = mutableSetOf<String>()
            
            // Add from Search API (The most accurate source for unique SHAs)
            searchItems.forEach { item ->
                if (isToday(item.commit.author.date)) {
                    uniqueCommitShas.add(item.sha)
                }
            }
            
            // Add from Events API (Catches real-time pushes for public & private)
            pushEvents.filter { isToday(it.createdAt) }.forEach { event ->
                event.payload?.commits?.forEach { commitRef ->
                    // Only count "distinct" commits to avoid merge-commit inflation
                    if (commitRef.distinct) {
                        uniqueCommitShas.add(commitRef.sha)
                    }
                }
            }

            val todayCommitCount = uniqueCommitShas.size
            
            // 5. Fetch History for Streak (Last 45 Days)
            val historyAgo = LocalDate.now(ZoneId.systemDefault()).minusDays(45).toString()
            val historyResponse = api.searchCommits("author:$resolvedUsername author-date:>$historyAgo", perPage = 100)
            val allHistoryCommits = historyResponse.body()?.items ?: emptyList()

            val mostRecent = pushEvents.firstOrNull()

            val status = CommitStatus(
                committedToday    = todayCommitCount >= commitLimit,
                todayCommitCount  = todayCommitCount,
                lastCommitTime    = mostRecent?.createdAt ?: searchItems.firstOrNull()?.commit?.author?.date,
                lastCommitRepo    = mostRecent?.repo?.name ?: searchItems.firstOrNull()?.repository?.name,
                lastCommitMessage = mostRecent?.payload?.commits?.firstOrNull()?.message ?: searchItems.firstOrNull()?.commit?.message,
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
