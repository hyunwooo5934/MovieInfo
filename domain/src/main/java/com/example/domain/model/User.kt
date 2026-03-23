package com.example.domain.model

data class User(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?,
    val loginType: SocialLoginType
)
