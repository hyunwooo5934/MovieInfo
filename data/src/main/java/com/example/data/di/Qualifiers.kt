package com.example.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GoogleWebClientId

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NaverClientId

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NaverClientSecret

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NaverClientName