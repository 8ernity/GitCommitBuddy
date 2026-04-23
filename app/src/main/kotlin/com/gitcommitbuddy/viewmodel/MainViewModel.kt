package com.gitcommitbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitcommitbuddy.data.PreferencesManager
import com.gitcommitbuddy.data.api.ApiResult
import com.gitcommitbuddy.data.api.CommitStatus
import com.gitcommitbuddy.data.api.GitHubUser
import com.gitcommitbuddy.data.db.CommitCacheEntity
import com.gitcommitbuddy.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GitHubRepository,
    private val prefs: PreferencesManager
) : ViewModel() {

    // ── UI state ──────────────────────────────────────────────────────────────

    private val _uiState = MutableStateFlow<CommitUiState>(CommitUiState.Idle)
    val uiState: StateFlow<CommitUiState> = _uiState.asStateFlow()

    private val _userProfile = MutableStateFlow<GitHubUser?>(null)
    val userProfile: StateFlow<GitHubUser?> = _userProfile.asStateFlow()

    val cachedStatus: StateFlow<CommitCacheEntity?> = repository
        .observeCommitStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val githubUsername: StateFlow<String> = prefs.githubUsername
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val widgetEnabled: StateFlow<Boolean> = prefs.widgetEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    // ── Actions ───────────────────────────────────────────────────────────────

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = CommitUiState.Loading
            val snapshot = prefs.getSnapshot()
            if (snapshot.username.isBlank()) {
                _uiState.value = CommitUiState.NotConfigured
                return@launch
            }
            _uiState.value = when (val result =
                repository.refreshCommitStatus(snapshot.username, snapshot.token, snapshot.commitLimit)) {
                is ApiResult.Success -> CommitUiState.Loaded(result.data)
                is ApiResult.Error   -> CommitUiState.Error(result.message)
                else                 -> CommitUiState.Loading
            }
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            val snapshot = prefs.getSnapshot()
            if (snapshot.username.isBlank()) return@launch
            val result = repository.fetchUserProfile(snapshot.username)
            if (result is ApiResult.Success) _userProfile.value = result.data
        }
    }

    fun setWidgetEnabled(enabled: Boolean) {
        viewModelScope.launch { prefs.setWidgetEnabled(enabled) }
    }
}

sealed class CommitUiState {
    object Idle          : CommitUiState()
    object Loading       : CommitUiState()
    object NotConfigured : CommitUiState()
    data class Loaded(val status: CommitStatus) : CommitUiState()
    data class Error(val message: String)       : CommitUiState()
}
