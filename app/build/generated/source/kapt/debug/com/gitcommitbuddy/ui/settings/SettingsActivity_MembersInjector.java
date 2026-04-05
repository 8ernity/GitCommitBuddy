package com.gitcommitbuddy.ui.settings;

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
public final class SettingsActivity_MembersInjector implements MembersInjector<SettingsActivity> {
  private final Provider<NotificationHelper> notifHelperProvider;

  public SettingsActivity_MembersInjector(Provider<NotificationHelper> notifHelperProvider) {
    this.notifHelperProvider = notifHelperProvider;
  }

  public static MembersInjector<SettingsActivity> create(
      Provider<NotificationHelper> notifHelperProvider) {
    return new SettingsActivity_MembersInjector(notifHelperProvider);
  }

  @Override
  public void injectMembers(SettingsActivity instance) {
    injectNotifHelper(instance, notifHelperProvider.get());
  }

  @InjectedFieldSignature("com.gitcommitbuddy.ui.settings.SettingsActivity.notifHelper")
  public static void injectNotifHelper(SettingsActivity instance, NotificationHelper notifHelper) {
    instance.notifHelper = notifHelper;
  }
}
