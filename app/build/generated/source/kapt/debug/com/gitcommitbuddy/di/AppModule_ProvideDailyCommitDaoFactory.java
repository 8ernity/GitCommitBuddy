package com.gitcommitbuddy.di;

import com.gitcommitbuddy.data.db.AppDatabase;
import com.gitcommitbuddy.data.db.DailyCommitDao;
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
public final class AppModule_ProvideDailyCommitDaoFactory implements Factory<DailyCommitDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideDailyCommitDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public DailyCommitDao get() {
    return provideDailyCommitDao(dbProvider.get());
  }

  public static AppModule_ProvideDailyCommitDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideDailyCommitDaoFactory(dbProvider);
  }

  public static DailyCommitDao provideDailyCommitDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDailyCommitDao(db));
  }
}
