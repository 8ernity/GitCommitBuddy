package com.gitcommitbuddy.data.repository;

import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.data.api.GitHubApiService;
import com.gitcommitbuddy.data.db.CommitCacheDao;
import com.gitcommitbuddy.data.db.DailyCommitDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class GitHubRepository_Factory implements Factory<GitHubRepository> {
  private final Provider<GitHubApiService> apiProvider;

  private final Provider<CommitCacheDao> cacheDaoProvider;

  private final Provider<DailyCommitDao> dailyDaoProvider;

  private final Provider<PreferencesManager> prefsProvider;

  public GitHubRepository_Factory(Provider<GitHubApiService> apiProvider,
      Provider<CommitCacheDao> cacheDaoProvider, Provider<DailyCommitDao> dailyDaoProvider,
      Provider<PreferencesManager> prefsProvider) {
    this.apiProvider = apiProvider;
    this.cacheDaoProvider = cacheDaoProvider;
    this.dailyDaoProvider = dailyDaoProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public GitHubRepository get() {
    return newInstance(apiProvider.get(), cacheDaoProvider.get(), dailyDaoProvider.get(), prefsProvider.get());
  }

  public static GitHubRepository_Factory create(Provider<GitHubApiService> apiProvider,
      Provider<CommitCacheDao> cacheDaoProvider, Provider<DailyCommitDao> dailyDaoProvider,
      Provider<PreferencesManager> prefsProvider) {
    return new GitHubRepository_Factory(apiProvider, cacheDaoProvider, dailyDaoProvider, prefsProvider);
  }

  public static GitHubRepository newInstance(GitHubApiService api, CommitCacheDao cacheDao,
      DailyCommitDao dailyDao, PreferencesManager prefs) {
    return new GitHubRepository(api, cacheDao, dailyDao, prefs);
  }
}
