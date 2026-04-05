package com.gitcommitbuddy.ui.main;

import com.gitcommitbuddy.data.PreferencesManager;
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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<NotificationHelper> notifHelperProvider;

  private final Provider<PreferencesManager> prefsProvider;

  public MainActivity_MembersInjector(Provider<NotificationHelper> notifHelperProvider,
      Provider<PreferencesManager> prefsProvider) {
    this.notifHelperProvider = notifHelperProvider;
    this.prefsProvider = prefsProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<NotificationHelper> notifHelperProvider,
      Provider<PreferencesManager> prefsProvider) {
    return new MainActivity_MembersInjector(notifHelperProvider, prefsProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectNotifHelper(instance, notifHelperProvider.get());
    injectPrefs(instance, prefsProvider.get());
  }

  @InjectedFieldSignature("com.gitcommitbuddy.ui.main.MainActivity.notifHelper")
  public static void injectNotifHelper(MainActivity instance, NotificationHelper notifHelper) {
    instance.notifHelper = notifHelper;
  }

  @InjectedFieldSignature("com.gitcommitbuddy.ui.main.MainActivity.prefs")
  public static void injectPrefs(MainActivity instance, PreferencesManager prefs) {
    instance.prefs = prefs;
  }
}
