package com.gitcommitbuddy.di;

import com.gitcommitbuddy.data.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.Interceptor;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideAuthInterceptorFactory implements Factory<Interceptor> {
  private final Provider<PreferencesManager> prefsManagerProvider;

  public AppModule_ProvideAuthInterceptorFactory(
      Provider<PreferencesManager> prefsManagerProvider) {
    this.prefsManagerProvider = prefsManagerProvider;
  }

  @Override
  public Interceptor get() {
    return provideAuthInterceptor(prefsManagerProvider.get());
  }

  public static AppModule_ProvideAuthInterceptorFactory create(
      Provider<PreferencesManager> prefsManagerProvider) {
    return new AppModule_ProvideAuthInterceptorFactory(prefsManagerProvider);
  }

  public static Interceptor provideAuthInterceptor(PreferencesManager prefsManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAuthInterceptor(prefsManager));
  }
}
