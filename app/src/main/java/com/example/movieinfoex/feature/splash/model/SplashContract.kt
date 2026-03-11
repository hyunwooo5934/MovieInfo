package com.example.movieinfoex.feature.splash.model


sealed interface SplashIntent {
    data object CheckAutoLogin : SplashIntent
}

data class SplashState(
    val isLoading: Boolean = true
)

sealed interface SplashEffect {
    data object NavigateLogin : SplashEffect
//    data object NavigateHome : SplashEffect
}
