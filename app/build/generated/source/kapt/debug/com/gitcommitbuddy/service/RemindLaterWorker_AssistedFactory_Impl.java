package com.gitcommitbuddy.service;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class RemindLaterWorker_AssistedFactory_Impl implements RemindLaterWorker_AssistedFactory {
  private final RemindLaterWorker_Factory delegateFactory;

  RemindLaterWorker_AssistedFactory_Impl(RemindLaterWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public RemindLaterWorker create(Context arg0, WorkerParameters arg1) {
    return delegateFactory.get(arg0, arg1);
  }

  public static Provider<RemindLaterWorker_AssistedFactory> create(
      RemindLaterWorker_Factory delegateFactory) {
    return InstanceFactory.create(new RemindLaterWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<RemindLaterWorker_AssistedFactory> createFactoryProvider(
      RemindLaterWorker_Factory delegateFactory) {
    return InstanceFactory.create(new RemindLaterWorker_AssistedFactory_Impl(delegateFactory));
  }
}
