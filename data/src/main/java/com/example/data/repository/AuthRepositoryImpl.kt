package com.example.data.repository

import com.example.data.SessionStorage
import com.example.data.auth.SocialAuthDataSource
import com.example.domain.model.AuthResult
import com.example.domain.model.AuthUser
import com.example.domain.model.SocialProvider
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSources: Set<@JvmSuppressWildcards SocialAuthDataSource>,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(provider: SocialProvider): AuthResult {
        val dataSource = dataSources.first { it.provider == provider }
        val result = dataSource.login()

        if (result is AuthResult.Success) {
            sessionStorage.saveUser(result.user)
            sessionStorage.saveCredential(result.credential)
        }
        return result
    }

    override suspend fun logout(provider: SocialProvider) {
        dataSources.first { it.provider == provider }.logout()
        sessionStorage.clear()
    }

    override suspend fun getSavedUser(): AuthUser? {
        return sessionStorage.getUser()
    }

    override suspend fun clearSession() {
        sessionStorage.clear()
    }
}