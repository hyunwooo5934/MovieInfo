package com.example.data.di

import android.content.Context
import com.example.data.R
import com.example.data.datasource.GoogleAuthDataSource
import com.example.data.local.UserDataStore
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.UserRepository
import com.example.snslogin.data.datasource.NaverAuthDataSource
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
    ): String = context.getString(R.string.GOOGLE_WEB_CLIENT_ID)

    @Provides
    @NaverClientId
    fun provideNaverClientId(
        @ApplicationContext context: Context
    ): String = "tOC113jg46p8vLrDGnMA"
//        context.getString(R.string.NAVER_CLIENT_ID)

    @Provides
    @NaverClientSecret
    fun provideNaverClientSecret(
        @ApplicationContext context: Context
    ): String = "XvH1dv838p"
//        context.getString(R.string.NAVER_CLIENT_SECRET)

    @Provides
    @NaverClientName
    fun provideNaverClientName(): String = "MovieInfo"


    // ── DataSource 제공 ─────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideGoogleAuthDataSource(
        @GoogleWebClientId webClientId: String,
        @ApplicationContext context: Context
    ): GoogleAuthDataSource = GoogleAuthDataSource(webClientId,context)

    @Provides
    @Singleton
    fun provideNaverAuthDataSource(): NaverAuthDataSource = NaverAuthDataSource()


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
        naverAuthDataSource: NaverAuthDataSource,
        userDataStore: UserDataStore,
        @ApplicationContext context: Context
    ): UserRepository = UserRepositoryImpl(
        googleDataSource, naverAuthDataSource,userDataStore, context
    )

}