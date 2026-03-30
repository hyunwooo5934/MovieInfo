package com.example.movieinfo.feature.login.model

import android.app.Activity
import com.example.domain.model.SocialLoginType

sealed interface LoginIntent {
    /** 소셜 로그인 버튼 클릭 */
    data class SocialLogin(
        val type: SocialLoginType,
        val activity: Activity
    ) : LoginIntent
}
