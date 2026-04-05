package com.gitcommitbuddy.data.repository;

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

  public GitHubRepository_Factory(Provider<GitHubApiService> apiProvider,
      Provider<CommitCacheDao> cacheDaoProvider, Provider<DailyCommitDao> dailyDaoProvider) {
    this.apiProvider = apiProvider;
    this.cacheDaoProvider = cacheDaoProvider;
    this.dailyDaoProvider = dailyDaoProvider;
  }

  @Override
  public GitHubRepository get() {
    return newInstance(apiProvider.get(), cacheDaoProvider.get(), dailyDaoProvider.get());
  }

  public static GitHubRepository_Factory create(Provider<GitHubApiService> apiProvider,
      Provider<CommitCacheDao> cacheDaoProvider, Provider<DailyCommitDao> dailyDaoProvider) {
    return new GitHubRepository_Factory(apiProvider, cacheDaoProvider, dailyDaoProvider);
  }

  public static GitHubRepository newInstance(GitHubApiService api, CommitCacheDao cacheDao,
      DailyCommitDao dailyDao) {
    return new GitHubRepository(api, cacheDao, dailyDao);
  }
}
