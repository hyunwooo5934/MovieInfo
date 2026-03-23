package com.example.data.repository

import android.content.Context
import com.example.data.datasource.GoogleAuthDataSource
import com.example.data.local.UserDataStore
import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import javax.inject.Inject


/**
 * UserRepository 구현체
 *
 * ─── 현재 구조 (서버 없음) ───────────────────────────────────────────
 * DataSource에서 토큰 획득 → 각 SDK API로 프로필 직접 조회 → User 반환
 *
 * ─── 추후 서버 추가 시 변경 위치 ─────────────────────────────────────
 * signIn() 내부에 remoteDataSource.verifyToken(token, loginType) 한 줄 추가
 * → ViewModel, UseCase, Screen 코드 변경 불필요
 * ─────────────────────────────────────────────────────────────────────
 */


class UserRepositoryImpl @Inject constructor(
    private val googleDataSource: GoogleAuthDataSource,
    private val userDataStore: UserDataStore,
    @ApplicationContext private val context: Context
) : UserRepository {

    // 현재 로그인된 사용자 (메모리 캐시)
    // 추후 DataStore 또는 EncryptedSharedPreferences로 영속화 가능
    private var cachedUser: User? = null

    override suspend fun signIn(
        token: String,
        loginType: SocialLoginType
    ): Result<User?> = try {
        val user = when (loginType) {
            SocialLoginType.GOOGLE -> fetchGoogleProfile(token)
            SocialLoginType.NAVER  -> fetchNaverProfile()
            SocialLoginType.KAKAO  -> fetchKakaoProfile()
        }
        // 로그인 성공 시 loginType 저장
        userDataStore.saveLoginType(loginType)
        cachedUser = user
        Result.success(user)

    } catch (e: CancellationException) {
        Result.failure(e)
    } catch (e: AppError) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(AppError.NetworkError.Unknown(e.message ?: ""))
    }

    override suspend fun getCurrentUser(): User? {
        // 1. 메모리 캐시 확인 (가장 빠름)
        cachedUser?.let { return it }

        // 2. DataStore에서 loginType 확인
        val loginType = userDataStore.getLoginType() ?: return null

        // 3. 해당 SDK 토큰 유효성 확인 후 User 반환
        return try {
            when (loginType) {
                SocialLoginType.GOOGLE -> googleDataSource.getSignedInUser()
                SocialLoginType.NAVER  -> googleDataSource.getSignedInUser()
                SocialLoginType.KAKAO  -> googleDataSource.getSignedInUser()
            }.also { user ->
                cachedUser = user // 조회 성공 시 캐시에 저장
            }
        } catch (e: Exception) {
            // SDK 토큰 만료 또는 오류 → DataStore 초기화
            userDataStore.clear()
            null
        }
    }

    override suspend fun signOut() {
        cachedUser?.let { user ->
            googleDataSource.logout()
//            when (user.loginType) {
//                SocialLoginType.GOOGLE -> googleDataSource.logout()
//                SocialLoginType.NAVER  ->
//                SocialLoginType.KAKAO  ->
//            }
        }
        userDataStore.clear()   // DataStore 초기화
        cachedUser = null       // 메모리 캐시 초기화
    }

    // ── 각 소셜 SDK 프로필 조회 ─────────────────────────────────────

    /**
     * Google: idToken에서 기본 정보 파싱
     * 추후 서버 추가 시 이 메서드 제거하고 서버 응답 사용
     */
    private fun fetchGoogleProfile(idToken: String): User {
        // idToken은 JWT — base64 디코딩으로 payload에서 정보 추출 가능
        // 여기서는 Credential 생성 시 받은 정보 활용 (ViewModel에서 넘겨받는 방식으로 개선 가능)
        return User(
            uid = idToken.take(20), // 임시 uid, 실제론 JWT sub 클레임 파싱
            email = "",             // 추후 JWT 파싱 또는 서버 응답으로 채움
            displayName = "",
            photoUrl = null,
            loginType = SocialLoginType.GOOGLE
        )
    }

    /** Naver: SDK API로 프로필 조회 */
    private suspend fun fetchNaverProfile(): User {
        return User(
            uid = "", // 임시 uid, 실제론 JWT sub 클레임 파싱
            email = "",             // 추후 JWT 파싱 또는 서버 응답으로 채움
            displayName = "",
            photoUrl = null,
            loginType = SocialLoginType.NAVER
        )
    }


    /** Kakao: SDK API로 프로필 조회 */
    private suspend fun fetchKakaoProfile(): User? {
        return User(
            uid = "", // 임시 uid, 실제론 JWT sub 클레임 파싱
            email = "",             // 추후 JWT 파싱 또는 서버 응답으로 채움
            displayName = "",
            photoUrl = null,
            loginType = SocialLoginType.KAKAO
        )
    }


}