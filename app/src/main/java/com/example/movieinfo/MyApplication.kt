package com.example.movieinfo

import android.app.Application
import com.example.data.di.naver.NaverSdkInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application() {


    // Hilt가 NaverSdkInitializer 주입
    @Inject lateinit var naverSdkInitializer: NaverSdkInitializer

    override fun onCreate() {
        super.onCreate()
        naverSdkInitializer.init()
    }

}