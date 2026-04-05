package com.gitcommitbuddy.service;

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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<PreferencesManager> prefsProvider;

  public BootReceiver_MembersInjector(Provider<PreferencesManager> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  public static MembersInjector<BootReceiver> create(Provider<PreferencesManager> prefsProvider) {
    return new BootReceiver_MembersInjector(prefsProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectPrefs(instance, prefsProvider.get());
  }

  @InjectedFieldSignature("com.gitcommitbuddy.service.BootReceiver.prefs")
  public static void injectPrefs(BootReceiver instance, PreferencesManager prefs) {
    instance.prefs = prefs;
  }
}
