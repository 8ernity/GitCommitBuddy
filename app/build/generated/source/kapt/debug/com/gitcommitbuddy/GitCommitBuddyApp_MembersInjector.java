package com.gitcommitbuddy;

import androidx.hilt.work.HiltWorkerFactory;
import com.gitcommitbuddy.data.PreferencesManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GitCommitBuddyApp_MembersInjector implements MembersInjector<GitCommitBuddyApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  private final Provider<PreferencesManager> prefsProvider;

  public GitCommitBuddyApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<PreferencesManager> prefsProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
    this.prefsProvider = prefsProvider;
  }

  public static MembersInjector<GitCommitBuddyApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<PreferencesManager> prefsProvider) {
    return new GitCommitBuddyApp_MembersInjector(workerFactoryProvider, prefsProvider);
  }

  @Override
  public void injectMembers(GitCommitBuddyApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
    injectPrefs(instance, prefsProvider.get());
  }

  @InjectedFieldSignature("com.gitcommitbuddy.GitCommitBuddyApp.workerFactory")
  public static void injectWorkerFactory(GitCommitBuddyApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }

  @InjectedFieldSignature("com.gitcommitbuddy.GitCommitBuddyApp.prefs")
  public static void injectPrefs(GitCommitBuddyApp instance, PreferencesManager prefs) {
    instance.prefs = prefs;
  }
}
