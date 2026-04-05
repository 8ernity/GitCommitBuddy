package com.gitcommitbuddy.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────────────────────
// Entity: cached commit status (one row — always upserted by primary key 1)
// ─────────────────────────────────────────────────────────────────────────────

@Entity(tableName = "commit_cache")
data class CommitCacheEntity(
    @PrimaryKey val id: Int = 1,               // Singleton row
    val committedToday: Boolean,
    val lastCommitTime: String?,
    val lastCommitRepo: String?,
    val lastCommitMessage: String?,
    val currentStreak: Int,
    val cachedAt: Long = System.currentTimeMillis() // epoch-ms
)

// ─────────────────────────────────────────────────────────────────────────────
// Entity: per-day commit log for streak calculation (one row per calendar day)
// ─────────────────────────────────────────────────────────────────────────────

@Entity(tableName = "daily_commits", primaryKeys = ["dateKey"])
data class DailyCommitEntity(
    val dateKey: String,     // "yyyy-MM-dd" in UTC
    val didCommit: Boolean,
    val commitCount: Int,
    val lastCommitTime: String?
)

// ─────────────────────────────────────────────────────────────────────────────
// DAOs
// ─────────────────────────────────────────────────────────────────────────────

@Dao
interface CommitCacheDao {

    @Query("SELECT * FROM commit_cache WHERE id = 1")
    fun observeCache(): Flow<CommitCacheEntity?>

    @Query("SELECT * FROM commit_cache WHERE id = 1")
    suspend fun getCache(): CommitCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CommitCacheEntity)

    @Query("DELETE FROM commit_cache")
    suspend fun clearAll()
}

@Dao
interface DailyCommitDao {

    @Query("SELECT * FROM daily_commits ORDER BY dateKey DESC LIMIT 90")
    fun observeRecentDays(): Flow<List<DailyCommitEntity>>

    @Query("SELECT * FROM daily_commits ORDER BY dateKey DESC LIMIT 90")
    suspend fun getRecentDays(): List<DailyCommitEntity>

    @Query("SELECT * FROM daily_commits WHERE dateKey = :dateKey")
    suspend fun getForDate(dateKey: String): DailyCommitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DailyCommitEntity)

    @Query("DELETE FROM daily_commits WHERE dateKey < :cutoffDate")
    suspend fun pruneOlderThan(cutoffDate: String)
}

// ─────────────────────────────────────────────────────────────────────────────
// Database
// ─────────────────────────────────────────────────────────────────────────────

@Database(
    entities = [CommitCacheEntity::class, DailyCommitEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commitCacheDao(): CommitCacheDao
    abstract fun dailyCommitDao(): DailyCommitDao
}
