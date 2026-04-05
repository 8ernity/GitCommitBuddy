package com.gitcommitbuddy.di

import android.content.Context
import androidx.room.Room
import com.gitcommitbuddy.data.PreferencesManager
import com.gitcommitbuddy.data.api.GitHubApiService
import com.gitcommitbuddy.data.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ── Room ──────────────────────────────────────────────────────────────────

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "gcb_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideCommitCacheDao(db: AppDatabase): CommitCacheDao = db.commitCacheDao()
    @Provides fun provideDailyCommitDao(db: AppDatabase): DailyCommitDao = db.dailyCommitDao()

    // ── Networking ────────────────────────────────────────────────────────────

    /**
     * Auth interceptor: reads the token from DataStore on every request.
     * runBlocking is acceptable here — Retrofit executes this on an IO thread,
     * never on the main thread.
     *
     * ⚠️ TOKEN INJECTION POINT — the PAT entered in Settings is added here as
     *    the Authorization header. Never hard-code tokens in source code.
     */
    @Provides @Singleton
    fun provideAuthInterceptor(prefsManager: PreferencesManager): Interceptor =
        Interceptor { chain ->
            val token = runBlocking { prefsManager.githubToken.first() }
            val builder = chain.request().newBuilder()
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
            if (token.isNotBlank()) {
                builder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(builder.build())
        }

    @Provides @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

    @Provides @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    @Provides @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideGitHubApiService(retrofit: Retrofit): GitHubApiService =
        retrofit.create(GitHubApiService::class.java)
}
