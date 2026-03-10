package com.example.data.auth.google

import com.example.data.auth.SocialAuthDataSource
import com.example.domain.model.AuthResult
import com.example.domain.model.SocialProvider
import javax.inject.Inject

class GoogleAuthDataSource @Inject constructor(
    private val client: GoogleAuthClient
) : SocialAuthDataSource {

    override val provider: SocialProvider = SocialProvider.GOOGLE

    override suspend fun login(): AuthResult = client.signIn()

    override suspend fun logout() = client.signOut()
}