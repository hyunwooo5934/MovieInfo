package com.example.domain.model

sealed interface AuthResult {
    data class Success(
        val user: AuthUser,
        val credential: AuthCredential
    ) : AuthResult

    data class Failure(
        val error: AuthError
    ) : AuthResult
}