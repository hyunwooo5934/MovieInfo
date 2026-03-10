package com.example.domain.model

sealed interface AuthError {
    data object Cancelled : AuthError
    data object Network : AuthError
    data object InvalidCredential : AuthError
    data object NotSupported : AuthError
    data class Unknown(val message: String? = null) : AuthError
}