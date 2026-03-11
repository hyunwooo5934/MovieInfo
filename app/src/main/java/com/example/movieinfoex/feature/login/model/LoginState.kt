package com.example.movieinfoex.feature.login.model

import com.example.domain.model.AuthUser

data class LoginState(
    val isLoading: Boolean = false,
    val user: AuthUser? = null,
    val isLoginSuccess: Boolean = false,
    val errorMessage: String? = null
)
