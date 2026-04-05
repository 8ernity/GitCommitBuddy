package com.gitcommitbuddy.di;

import com.gitcommitbuddy.data.db.AppDatabase;
import com.gitcommitbuddy.data.db.CommitCacheDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AppModule_ProvideCommitCacheDaoFactory implements Factory<CommitCacheDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideCommitCacheDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CommitCacheDao get() {
    return provideCommitCacheDao(dbProvider.get());
  }

  public static AppModule_ProvideCommitCacheDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideCommitCacheDaoFactory(dbProvider);
  }

  public static CommitCacheDao provideCommitCacheDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCommitCacheDao(db));
  }
}
