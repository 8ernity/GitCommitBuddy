package com.gitcommitbuddy.viewmodel;

import androidx.lifecycle.ViewModel;
import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.data.api.ApiResult;
import com.gitcommitbuddy.data.api.CommitStatus;
import com.gitcommitbuddy.data.api.GitHubUser;
import com.gitcommitbuddy.data.db.CommitCacheEntity;
import com.gitcommitbuddy.data.repository.GitHubRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u001b\u001a\u00020\u001cJ\u0006\u0010\u001d\u001a\u00020\u001cJ\u000e\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020\u0019R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u0019\u0010\u0016\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0010\u00a8\u0006 "}, d2 = {"Lcom/gitcommitbuddy/viewmodel/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/gitcommitbuddy/data/repository/GitHubRepository;", "prefs", "Lcom/gitcommitbuddy/data/PreferencesManager;", "(Lcom/gitcommitbuddy/data/repository/GitHubRepository;Lcom/gitcommitbuddy/data/PreferencesManager;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/gitcommitbuddy/viewmodel/CommitUiState;", "_userProfile", "Lcom/gitcommitbuddy/data/api/GitHubUser;", "cachedStatus", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/gitcommitbuddy/data/db/CommitCacheEntity;", "getCachedStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "githubUsername", "", "getGithubUsername", "uiState", "getUiState", "userProfile", "getUserProfile", "widgetEnabled", "", "getWidgetEnabled", "fetchProfile", "", "refresh", "setWidgetEnabled", "enabled", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MainViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.repository.GitHubRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.gitcommitbuddy.data.PreferencesManager prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.gitcommitbuddy.viewmodel.CommitUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.gitcommitbuddy.viewmodel.CommitUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.gitcommitbuddy.data.api.GitHubUser> _userProfile = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.gitcommitbuddy.data.api.GitHubUser> userProfile = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.gitcommitbuddy.data.db.CommitCacheEntity> cachedStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> githubUsername = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> widgetEnabled = null;
    
    @javax.inject.Inject()
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.repository.GitHubRepository repository, @org.jetbrains.annotations.NotNull()
    com.gitcommitbuddy.data.PreferencesManager prefs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.gitcommitbuddy.viewmodel.CommitUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.gitcommitbuddy.data.api.GitHubUser> getUserProfile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.gitcommitbuddy.data.db.CommitCacheEntity> getCachedStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getGithubUsername() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getWidgetEnabled() {
        return null;
    }
    
    public final void refresh() {
    }
    
    public final void fetchProfile() {
    }
    
    public final void setWidgetEnabled(boolean enabled) {
    }
}