package com.gitcommitbuddy

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.gitcommitbuddy.data.PreferencesManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Application class — entry point for Hilt dependency injection and
 * custom WorkManager initialisation (manual init so Hilt can inject
 * into Workers).
 */
@HiltAndroidApp
class GitCommitBuddyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var prefs: PreferencesManager

    override fun onCreate() {
        super.onCreate()

        // Apply dark mode globally on startup to prevent flickering
        MainScope().launch {
            val snapshot = prefs.getSnapshot()
            AppCompatDelegate.setDefaultNightMode(
                if (snapshot.darkMode) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
