package com.example.domain.model

data class AuthCredential(
    val provider: SocialProvider,
    val accessToken: String? = null,
    val idToken: String? = null,
    val refreshToken: String? = null,
    val authCode: String? = null
)
