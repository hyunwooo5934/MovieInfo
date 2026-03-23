package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import javax.inject.Inject


/**
 * 현재 로그인 상태 확인 UseCase
 *
 * Splash 화면에서 자동 로그인 여부 판단에 사용
 */
class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): User? = userRepository.getCurrentUser()

}