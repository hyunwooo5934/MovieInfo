package com.example.data.auth

import com.example.domain.model.AuthResult
import com.example.domain.model.SocialProvider

interface SocialAuthDataSource {
    val provider: SocialProvider
    suspend fun login(): AuthResult
    suspend fun logout()
}