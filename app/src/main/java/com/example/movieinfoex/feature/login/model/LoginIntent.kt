package com.example.movieinfoex.feature.login.model

sealed interface LoginIntent {
    data object ClickGoogle : LoginIntent
    data object ClickKakao : LoginIntent
    data object ClickNaver : LoginIntent
    data object CheckAutoLogin : LoginIntent
}