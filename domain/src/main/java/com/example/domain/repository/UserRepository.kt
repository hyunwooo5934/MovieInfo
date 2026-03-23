package com.example.domain.repository

import com.example.domain.model.SocialLoginType
import com.example.domain.model.User

interface UserRepository {

    /**
     * 소셜 로그인 토큰으로 사용자 인증
     *
     * @param token Google idToken / Naver accessToken / Kakao accessToken
     * @param loginType 로그인 종류
     * @return Result.success(User) or Result.failure(AppError)
     *
     * 추후 서버 검증 추가 위치:
     * UserRepositoryImpl.signIn() 내부에만 remoteDataSource.verifyToken() 추가
     * → 나머지 레이어 코드 변경 불필요
     */
    suspend fun signIn(token: String, loginType: SocialLoginType): Result<User?>

    /** 현재 로그인된 사용자 반환 (없으면 null) */
    suspend fun getCurrentUser(): User?

    /** 로그아웃 */
    suspend fun signOut()

}