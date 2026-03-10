package com.example.domain.usecase

import com.example.domain.model.AuthUser
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class GetSavedUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AuthUser? = repository.getSavedUser()
}