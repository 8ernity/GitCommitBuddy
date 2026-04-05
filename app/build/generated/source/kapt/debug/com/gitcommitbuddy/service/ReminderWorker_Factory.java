package com.gitcommitbuddy.service;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.data.repository.GitHubRepository;
import com.gitcommitbuddy.util.NotificationHelper;
import dagger.internal.DaggerGenerated;
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
public final class ReminderWorker_Factory {
  private final Provider<GitHubRepository> repositoryProvider;

  private final Provider<PreferencesManager> prefsProvider;

  private final Provider<NotificationHelper> notifHelperProvider;

  public ReminderWorker_Factory(Provider<GitHubRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider,
      Provider<NotificationHelper> notifHelperProvider) {
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
    this.notifHelperProvider = notifHelperProvider;
  }

  public ReminderWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get(), prefsProvider.get(), notifHelperProvider.get());
  }

  public static ReminderWorker_Factory create(Provider<GitHubRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider,
      Provider<NotificationHelper> notifHelperProvider) {
    return new ReminderWorker_Factory(repositoryProvider, prefsProvider, notifHelperProvider);
  }

  public static ReminderWorker newInstance(Context context, WorkerParameters workerParams,
      GitHubRepository repository, PreferencesManager prefs, NotificationHelper notifHelper) {
    return new ReminderWorker(context, workerParams, repository, prefs, notifHelper);
  }
}
