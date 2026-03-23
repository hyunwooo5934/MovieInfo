package com.example.domain.usecase

import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import javax.inject.Inject


/**
 * 소셜 로그인 UseCase
 *
 * 단일 책임: 소셜 로그인 토큰을 받아 사용자 인증 처리
 * DataSource에서 받은 토큰 → Repository로 전달 → Result<User> 반환
 */
class SocialLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        token: String,
        loginTpye: SocialLoginType
    ): Result<User?> = userRepository.signIn(token,loginTpye)

}