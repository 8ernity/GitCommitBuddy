package com.gitcommitbuddy.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gcb_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val GITHUB_USERNAME  = stringPreferencesKey("github_username")
        val GITHUB_TOKEN     = stringPreferencesKey("github_token")
        val REMINDER_HOUR    = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE  = intPreferencesKey("reminder_minute")
        val WIDGET_ENABLED   = booleanPreferencesKey("widget_enabled")
        val NOTIFICATIONS_ON = booleanPreferencesKey("notifications_on")
        val DARK_MODE        = booleanPreferencesKey("dark_mode")
        val FIRST_LAUNCH     = booleanPreferencesKey("first_launch")
        val COMMIT_LIMIT     = intPreferencesKey("commit_limit")
    }

    object Defaults {
        const val REMINDER_HOUR   = 21
        const val REMINDER_MINUTE = 0
        const val WIDGET_ENABLED  = true
        const val NOTIFICATIONS   = true
        const val COMMIT_LIMIT    = 1
    }

    val githubUsername: Flow<String> = context.dataStore.data
        .catchIO().map { it[GITHUB_USERNAME] ?: "" }

    val githubToken: Flow<String> = context.dataStore.data
        .catchIO().map { it[GITHUB_TOKEN] ?: "" }

    val reminderHour: Flow<Int> = context.dataStore.data
        .catchIO().map { it[REMINDER_HOUR] ?: Defaults.REMINDER_HOUR }

    val reminderMinute: Flow<Int> = context.dataStore.data
        .catchIO().map { it[REMINDER_MINUTE] ?: Defaults.REMINDER_MINUTE }

    val widgetEnabled: Flow<Boolean> = context.dataStore.data
        .catchIO().map { it[WIDGET_ENABLED] ?: Defaults.WIDGET_ENABLED }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .catchIO().map { it[NOTIFICATIONS_ON] ?: Defaults.NOTIFICATIONS }

    val darkMode: Flow<Boolean> = context.dataStore.data
        .catchIO().map { it[DARK_MODE] ?: false }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data
        .catchIO().map { it[FIRST_LAUNCH] ?: true }

    val commitLimit: Flow<Int> = context.dataStore.data
        .catchIO().map { it[COMMIT_LIMIT] ?: Defaults.COMMIT_LIMIT }

    suspend fun saveGitHubCredentials(username: String, token: String) {
        context.dataStore.edit { prefs ->
            prefs[GITHUB_USERNAME] = username
            prefs[GITHUB_TOKEN]    = token
        }
    }

    suspend fun saveReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[REMINDER_HOUR]   = hour
            prefs[REMINDER_MINUTE] = minute
        }
    }

    suspend fun setWidgetEnabled(enabled: Boolean) {
        context.dataStore.edit { it[WIDGET_ENABLED] = enabled }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ON] = enabled }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun setCommitLimit(limit: Int) {
        context.dataStore.edit { it[COMMIT_LIMIT] = limit }
    }

    suspend fun markFirstLaunchComplete() {
        context.dataStore.edit { it[FIRST_LAUNCH] = false }
    }

    /** Safe one-shot read via Flow.first() — works in Workers and Services. */
    suspend fun getSnapshot(): AppPrefsSnapshot {
        val prefs = context.dataStore.data.catchIO().first()
        return AppPrefsSnapshot(
            username        = prefs[GITHUB_USERNAME] ?: "",
            token           = prefs[GITHUB_TOKEN] ?: "",
            reminderHour    = prefs[REMINDER_HOUR] ?: Defaults.REMINDER_HOUR,
            reminderMinute  = prefs[REMINDER_MINUTE] ?: Defaults.REMINDER_MINUTE,
            widgetEnabled   = prefs[WIDGET_ENABLED] ?: Defaults.WIDGET_ENABLED,
            notificationsOn = prefs[NOTIFICATIONS_ON] ?: Defaults.NOTIFICATIONS,
            darkMode        = prefs[DARK_MODE] ?: false,
            commitLimit     = prefs[COMMIT_LIMIT] ?: Defaults.COMMIT_LIMIT
        )
    }

    private fun Flow<Preferences>.catchIO() = catch { e ->
        if (e is IOException) emit(emptyPreferences()) else throw e
    }
}

data class AppPrefsSnapshot(
    val username: String         = "",
    val token: String            = "",
    val reminderHour: Int        = PreferencesManager.Defaults.REMINDER_HOUR,
    val reminderMinute: Int      = PreferencesManager.Defaults.REMINDER_MINUTE,
    val widgetEnabled: Boolean   = true,
    val notificationsOn: Boolean = true,
    val darkMode: Boolean        = false,
    val commitLimit: Int         = PreferencesManager.Defaults.COMMIT_LIMIT
)
