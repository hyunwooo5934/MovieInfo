package com.example.domain.repository

import com.example.domain.model.AuthResult
import com.example.domain.model.AuthUser
import com.example.domain.model.SocialProvider

interface AuthRepository {
    suspend fun login(provider: SocialProvider): AuthResult
    suspend fun logout(provider: SocialProvider)
    suspend fun getSavedUser(): AuthUser?
    suspend fun clearSession()
}