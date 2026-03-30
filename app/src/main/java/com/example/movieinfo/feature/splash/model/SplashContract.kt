package com.example.movieinfo.feature.splash.model

import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User


sealed interface SplashState {
    /** 초기 상태 또는 로그인 취소 후 상태 */
    data object Idle : SplashState

    /**
     * 로그인 진행 중
     * @param type 현재 진행 중인 소셜 로그인 종류 (해당 버튼만 로딩 표시)
     */
    data class Loading(val type: SocialLoginType) : SplashState

    /**
     * 로그인 성공
     * @param user 로그인된 사용자 정보
     */
    data class Success(val user: User?) : SplashState

    /**
     * 로그인 실패
     * @param error AppError 타입으로 에러 종류 구분 가능
     */
    data class Failure(val error: AppError) : SplashState
}

sealed interface SplashEffect {
    data object NavigateLogin : SplashEffect
    data object NavigateMain : SplashEffect
}
