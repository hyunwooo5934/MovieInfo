package com.example.domain.usecase

import com.example.domain.model.AuthResult
import com.example.domain.model.AuthUser
import com.example.domain.model.SocialProvider
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class  LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: SocialProvider): AuthResult {
        return authRepository.login(provider)
    }

//    suspend operator fun invoke(): AuthUser? = authRepository.getSavedUser()
//
//    suspend operator fun invoke(provider: SocialProvider) {
//        authRepository.logout(provider)
//    }
}