package com.example.movieinfoex.feature.login.model

sealed interface LoginEffect {
    data class ShowToast(val message: String) : LoginEffect
    data object NavigateHome : LoginEffect
}