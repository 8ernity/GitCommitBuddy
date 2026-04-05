package com.gitcommitbuddy.service;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.gitcommitbuddy.util.NotificationHelper;
import dagger.internal.DaggerGenerated;
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
public final class RemindLaterWorker_Factory {
  private final Provider<NotificationHelper> notifHelperProvider;

  public RemindLaterWorker_Factory(Provider<NotificationHelper> notifHelperProvider) {
    this.notifHelperProvider = notifHelperProvider;
  }

  public RemindLaterWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, notifHelperProvider.get());
  }

  public static RemindLaterWorker_Factory create(Provider<NotificationHelper> notifHelperProvider) {
    return new RemindLaterWorker_Factory(notifHelperProvider);
  }

  public static RemindLaterWorker newInstance(Context context, WorkerParameters workerParams,
      NotificationHelper notifHelper) {
    return new RemindLaterWorker(context, workerParams, notifHelper);
  }
}
