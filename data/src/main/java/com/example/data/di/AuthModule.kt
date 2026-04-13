package com.example.data.di

import android.content.Context
import com.example.data.BuildConfig
import com.example.data.datasource.GoogleAuthDataSource
import com.example.data.datasource.KakaoAuthDataSource
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
    fun provideGoogleWebClientId(): String = BuildConfig.GOOGLE_WEB_CLIENT_ID

    @Provides
    @NaverClientId
    fun provideNaverClientId(): String = BuildConfig.NAVER_CLIENT_ID

    @Provides
    @NaverClientSecret
    fun provideNaverClientSecret(): String = BuildConfig.NAVER_CLIENT_SECRET

    @Provides
    @Singleton
    fun provideKakaoClientId(): String = BuildConfig.KAKAO_CLIENT_ID

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

    @Provides
    @Singleton
    fun provideKakaoAuthDataSource(
        @ApplicationContext context: Context
    ): KakaoAuthDataSource = KakaoAuthDataSource(context)


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
        kakaoAuthDataSource: KakaoAuthDataSource,
        userDataStore: UserDataStore,
        @ApplicationContext context: Context
    ): UserRepository = UserRepositoryImpl(
        googleDataSource, naverAuthDataSource,kakaoAuthDataSource,userDataStore, context
    )

}