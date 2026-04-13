package com.example.movieinfo.feature.splash.model

import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User


sealed interface SplashState {
    /** 초기 상태 또는 로그인 취소 후 상태 */
    data object Idle : SplashState

    /**
     * 로그인 성공
     * @param user 로그인된 사용자 정보
     */
    data class Success(val user: User?) : SplashState

}

sealed interface SplashEffect {
    data object NavigateLogin : SplashEffect
    data object NavigateMain : SplashEffect
}
