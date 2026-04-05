package com.gitcommitbuddy.service;

import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.data.repository.GitHubRepository;
import com.gitcommitbuddy.util.NotificationHelper;
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
public final class FloatingWidgetService_MembersInjector implements MembersInjector<FloatingWidgetService> {
  private final Provider<GitHubRepository> repositoryProvider;

  private final Provider<PreferencesManager> prefsProvider;

  private final Provider<NotificationHelper> notifHelperProvider;

  public FloatingWidgetService_MembersInjector(Provider<GitHubRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider,
      Provider<NotificationHelper> notifHelperProvider) {
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
    this.notifHelperProvider = notifHelperProvider;
  }

  public static MembersInjector<FloatingWidgetService> create(
      Provider<GitHubRepository> repositoryProvider, Provider<PreferencesManager> prefsProvider,
      Provider<NotificationHelper> notifHelperProvider) {
    return new FloatingWidgetService_MembersInjector(repositoryProvider, prefsProvider, notifHelperProvider);
  }

  @Override
  public void injectMembers(FloatingWidgetService instance) {
    injectRepository(instance, repositoryProvider.get());
    injectPrefs(instance, prefsProvider.get());
    injectNotifHelper(instance, notifHelperProvider.get());
  }

  @InjectedFieldSignature("com.gitcommitbuddy.service.FloatingWidgetService.repository")
  public static void injectRepository(FloatingWidgetService instance, GitHubRepository repository) {
    instance.repository = repository;
  }

  @InjectedFieldSignature("com.gitcommitbuddy.service.FloatingWidgetService.prefs")
  public static void injectPrefs(FloatingWidgetService instance, PreferencesManager prefs) {
    instance.prefs = prefs;
  }

  @InjectedFieldSignature("com.gitcommitbuddy.service.FloatingWidgetService.notifHelper")
  public static void injectNotifHelper(FloatingWidgetService instance,
      NotificationHelper notifHelper) {
    instance.notifHelper = notifHelper;
  }
}
