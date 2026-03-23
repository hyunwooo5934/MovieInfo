package com.example.data.datasource

import android.app.Activity

/**
 * 소셜 로그인 DataSource 공통 인터페이스
 *
 * 각 소셜 SDK(Google/Naver/Kakao)의 구현 세부사항을 숨기고
 * Repository가 동일한 인터페이스로 사용할 수 있게 함
 *
 * 반환값: Result<String>
 * - 성공: Result.success(token)
 *   · Google → idToken (JWT)
 *   · Naver  → accessToken
 *   · Kakao  → accessToken
 * - 실패: Result.failure(AppError.AuthError.xxx)
 */
interface SocialAuthDataSource {
    suspend fun login(activity: Activity): Result<String>
    suspend fun logout()
}