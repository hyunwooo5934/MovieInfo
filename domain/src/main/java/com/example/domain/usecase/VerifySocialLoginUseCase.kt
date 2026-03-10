package com.example.domain.usecase

import com.example.domain.model.AuthCredential
import javax.inject.Inject

class VerifySocialLoginUseCase @Inject constructor() {
    suspend operator fun invoke(credential: AuthCredential): Boolean {
        // TODO 추후 서버 검증 API 연동
        return true
    }
}