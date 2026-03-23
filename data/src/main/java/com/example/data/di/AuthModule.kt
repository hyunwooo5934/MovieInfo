package com.example.data.di

import android.content.Context
import com.example.data.R
import com.example.data.datasource.GoogleAuthDataSource
import com.example.data.local.UserDataStore
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    // ── 키 / 시크릿 제공 ────────────────────────────────────────────

    @Provides
    @GoogleWebClientId
    fun provideGoogleWebClientId(
        @ApplicationContext context: Context
    ): String = context.getString(R.string.google_client_id)

//    @Provides
//    @NaverClientId
//    fun provideNaverClientId(): String = BuildConfig.NAVER_CLIENT_ID
//
//    @Provides
//    @NaverClientSecret
//    fun provideNaverClientSecret(): String = BuildConfig.NAVER_CLIENT_SECRET

    // ── DataSource 제공 ─────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideGoogleAuthDataSource(
        @GoogleWebClientId webClientId: String,
        @ApplicationContext context: Context
    ): GoogleAuthDataSource = GoogleAuthDataSource(webClientId,context)



    // ── Repository 제공 ─────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): UserDataStore = UserDataStore(context)

    @Provides
    @Singleton
    fun provideUserRepository(
        googleDataSource: GoogleAuthDataSource,
        userDataStore: UserDataStore,
        @ApplicationContext context: Context
    ): UserRepository = UserRepositoryImpl(
        googleDataSource, userDataStore, context
    )

}