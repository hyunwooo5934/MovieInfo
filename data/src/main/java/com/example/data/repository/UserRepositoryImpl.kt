package com.example.data.repository

import android.content.Context
import com.example.data.datasource.GoogleAuthDataSource
import com.example.data.datasource.KakaoAuthDataSource
import com.example.data.local.UserDataStore
import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.snslogin.data.datasource.NaverAuthDataSource
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.profile.domain.vo.NidProfile
import com.navercorp.nid.profile.util.NidProfileCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


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
    private val naverAuthDataSource: NaverAuthDataSource,
    private val kakaoAuthDataSource: KakaoAuthDataSource,
    private val userDataStore: UserDataStore,
    @ApplicationContext private val context: Context
) : UserRepository {

    // 현재 로그인된 사용자 (메모리 캐시)
    // 추후 DataStore 또는 EncryptedSharedPreferences로 영속화 가능
    private var currentUser: User? = null

    override suspend fun signIn(
        token: String,
        loginType: SocialLoginType
    ): Result<User?> = try {
        val user = when (loginType) {
            SocialLoginType.GOOGLE -> googleDataSource.fetchGoogleProfile(token)
            SocialLoginType.NAVER  -> naverAuthDataSource.fetchNaverProfile()
            SocialLoginType.KAKAO  -> kakaoAuthDataSource.fetchKakaoProfile()
        }
        // 로그인 성공 시 loginType 저장
        userDataStore.saveLoginType(loginType)
        userDataStore.saveLoginInfo(user)
        currentUser = user
        Result.success(user)

    } catch (e: CancellationException) {
        Result.failure(e)
    } catch (e: AppError) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(AppError.NetworkError.Unknown(e.message ?: ""))
    }

    override suspend fun getCurrentUser(): User? = userDataStore.getLoginInfo()

    override suspend fun signOut() {
        currentUser?.let { user ->
            when (user.loginType) {
                SocialLoginType.GOOGLE -> googleDataSource.logout()
                SocialLoginType.NAVER  -> naverAuthDataSource.logout()
                SocialLoginType.KAKAO  -> kakaoAuthDataSource.logout()
            }
        }
        userDataStore.clear()   // DataStore 초기화
        currentUser = null       // 메모리 캐시 초기화
    }

}