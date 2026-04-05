package com.gitcommitbuddy.viewmodel;

import com.gitcommitbuddy.data.PreferencesManager;
import com.gitcommitbuddy.data.repository.GitHubRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<GitHubRepository> repositoryProvider;

  private final Provider<PreferencesManager> prefsProvider;

  public MainViewModel_Factory(Provider<GitHubRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider) {
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(repositoryProvider.get(), prefsProvider.get());
  }

  public static MainViewModel_Factory create(Provider<GitHubRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider) {
    return new MainViewModel_Factory(repositoryProvider, prefsProvider);
  }

  public static MainViewModel newInstance(GitHubRepository repository, PreferencesManager prefs) {
    return new MainViewModel(repository, prefs);
  }
}
