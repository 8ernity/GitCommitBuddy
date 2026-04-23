package com.gitcommitbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitcommitbuddy.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferencesManager
) : ViewModel() {

    // Use MutableStateFlow to provide immediate UI updates and survive configuration changes (like dark mode toggle)
    private val _widgetEnabled = MutableStateFlow(true)
    val widgetEnabled = _widgetEnabled.asStateFlow()

    private val _notifEnabled = MutableStateFlow(true)
    val notifEnabled = _notifEnabled.asStateFlow()

    private val _darkMode = MutableStateFlow(false)
    val darkMode = _darkMode.asStateFlow()

    val username       = prefs.githubUsername.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
    val token          = prefs.githubToken.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
    val reminderHour   = prefs.reminderHour.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 21)
    val reminderMinute = prefs.reminderMinute.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val commitLimit     = prefs.commitLimit.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PreferencesManager.Defaults.COMMIT_LIMIT)

    init {
        // Synchronize our MutableStateFlows with the persistent DataStore
        viewModelScope.launch {
            prefs.widgetEnabled.collect { _widgetEnabled.value = it }
        }
        viewModelScope.launch {
            prefs.notificationsEnabled.collect { _notifEnabled.value = it }
        }
        viewModelScope.launch {
            prefs.darkMode.collect { _darkMode.value = it }
        }
    }

    fun saveCredentials(username: String, token: String) {
        viewModelScope.launch { prefs.saveGitHubCredentials(username, token) }
    }

    fun saveReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch { prefs.saveReminderTime(hour, minute) }
    }

    fun setWidgetEnabled(enabled: Boolean) {
        _widgetEnabled.value = enabled
        viewModelScope.launch { prefs.setWidgetEnabled(enabled) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _notifEnabled.value = enabled
        viewModelScope.launch { prefs.setNotificationsEnabled(enabled) }
    }

    fun setDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
        viewModelScope.launch { prefs.setDarkMode(enabled) }
    }

    fun setCommitLimit(limit: Int) {
        viewModelScope.launch { prefs.setCommitLimit(limit) }
    }
}
