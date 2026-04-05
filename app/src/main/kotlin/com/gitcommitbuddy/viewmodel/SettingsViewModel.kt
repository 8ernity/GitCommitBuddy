package com.gitcommitbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitcommitbuddy.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferencesManager
) : ViewModel() {

    val username        = prefs.githubUsername.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
    val token           = prefs.githubToken.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
    val reminderHour    = prefs.reminderHour.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 21)
    val reminderMinute  = prefs.reminderMinute.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val widgetEnabled   = prefs.widgetEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val notifEnabled    = prefs.notificationsEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val darkMode        = prefs.darkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val bubbleColor     = prefs.bubbleColor.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PreferencesManager.Defaults.BUBBLE_COLOR)

    fun saveCredentials(username: String, token: String) {
        viewModelScope.launch { prefs.saveGitHubCredentials(username, token) }
    }

    fun saveReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch { prefs.saveReminderTime(hour, minute) }
    }

    fun setWidgetEnabled(enabled: Boolean) {
        viewModelScope.launch { prefs.setWidgetEnabled(enabled) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { prefs.setNotificationsEnabled(enabled) }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { prefs.setDarkMode(enabled) }
    }

    fun setBubbleColor(color: String) {
        viewModelScope.launch { prefs.setBubbleColor(color) }
    }
}
