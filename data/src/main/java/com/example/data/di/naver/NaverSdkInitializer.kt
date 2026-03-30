package com.example.data.di.naver

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import com.example.data.di.NaverClientId
import com.example.data.di.NaverClientName
import com.example.data.di.NaverClientSecret
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.core.data.datastore.NidOAuthInitializingCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NaverSdkInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    @NaverClientId private val clientId: String,
    @NaverClientSecret private val clientSecret: String,
    @NaverClientName private val clientName: String,
){

    fun init() {
        NidOAuth.initialize(
            context      = context,
            clientId     = clientId,
            clientSecret = clientSecret,
            clientName   = clientName,
            callback     = object : NidOAuthInitializingCallback {

                override fun onSuccess() {
                    Log.d(TAG, "네아로SDK 초기화 성공 (v${NidOAuth.getVersion()})")
                }

                override fun onFailure(e: Exception) {
                    Log.e(TAG, "네아로SDK 초기화 실패", e)
                }
            }
        )

        // ⚠️ 배포 버전에는 반드시 false — 토큰 등 민감 정보가 로그에 노출됩니다.
        val isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        NidOAuth.setLogEnabled(isDebug)
    }

    companion object {
        private const val TAG = "NaverSdkInitializer"
    }

}