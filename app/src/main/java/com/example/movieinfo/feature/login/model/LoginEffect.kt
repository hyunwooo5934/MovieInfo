package com.example.movieinfo.feature.login.model

sealed interface LoginEffect {

    /** 메인 화면으로 이동 */
    data object NavigateToMain : LoginEffect

    /**
     * 에러 메시지 표시 (Snackbar)
     * @param message 사용자에게 보여줄 메시지
     */
    data class ShowError(val message: String) : LoginEffect
}