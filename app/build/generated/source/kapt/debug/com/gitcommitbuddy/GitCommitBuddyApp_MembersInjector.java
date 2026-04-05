package com.gitcommitbuddy;

import androidx.hilt.work.HiltWorkerFactory;
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

  public GitCommitBuddyApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<GitCommitBuddyApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new GitCommitBuddyApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(GitCommitBuddyApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.gitcommitbuddy.GitCommitBuddyApp.workerFactory")
  public static void injectWorkerFactory(GitCommitBuddyApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
