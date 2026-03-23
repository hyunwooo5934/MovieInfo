package com.example.movieinfoex.core.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {

    @Binds
    abstract fun bindSessionStorage(
        impl: SessionStorageImpl
    ): SessionStorage

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @IntoSet
    abstract fun bindGoogleDataSource(
        impl: GoogleAuthDataSource
    ): SocialAuthDataSource

//    @Binds
//    @IntoSet
//    abstract fun bindKakaoDataSource(
//        impl: KakaoAuthDataSource
//    ): SocialAuthDataSource
//
//    @Binds
//    @IntoSet
//    abstract fun bindNaverDataSource(
//        impl: NaverAuthDataSource
//    ): SocialAuthDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object AuthProvideModule {

    @Provides
    @Singleton
    @Named("google_web_client_id")
    fun provideGoogleWebClientId(): String {
        return "Web Client ID"
    }

//    @Provides
//    @Singleton
//    @Named("naver_client_id")
//    fun provideNaverClientId(): String = "YOUR_NAVER_CLIENT_ID"
//
//    @Provides
//    @Singleton
//    @Named("naver_client_secret")
//    fun provideNaverClientSecret(): String = "YOUR_NAVER_CLIENT_SECRET"
//
//    @Provides
//    @Singleton
//    @Named("naver_client_name")
//    fun provideNaverClientName(): String = "YOUR_APP_NAME"
}