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
public final class FollowUpReminderWorker_AssistedFactory_Impl implements FollowUpReminderWorker_AssistedFactory {
  private final FollowUpReminderWorker_Factory delegateFactory;

  FollowUpReminderWorker_AssistedFactory_Impl(FollowUpReminderWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public FollowUpReminderWorker create(Context arg0, WorkerParameters arg1) {
    return delegateFactory.get(arg0, arg1);
  }

  public static Provider<FollowUpReminderWorker_AssistedFactory> create(
      FollowUpReminderWorker_Factory delegateFactory) {
    return InstanceFactory.create(new FollowUpReminderWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<FollowUpReminderWorker_AssistedFactory> createFactoryProvider(
      FollowUpReminderWorker_Factory delegateFactory) {
    return InstanceFactory.create(new FollowUpReminderWorker_AssistedFactory_Impl(delegateFactory));
  }
}
