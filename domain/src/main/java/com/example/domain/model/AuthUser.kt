package com.example.domain.model

data class AuthUser(
    val provider: SocialProvider,
    val userId: String,
    val email: String?,
    val name: String?,
    val profileImageUrl: String?
)
