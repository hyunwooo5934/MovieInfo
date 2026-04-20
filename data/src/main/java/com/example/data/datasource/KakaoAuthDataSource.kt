package com.example.data.datasource

import android.app.Activity
import android.content.Context
import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Kakao 로그인 DataSource
 *
 * Kakao SDK v2 사용
 * 반환: accessToken
 * → 추후 서버 검증 시 이 accessToken을 서버로 전달
 *
 * 카카오톡 앱 설치 여부에 따라 자동 분기:
 * - 카카오톡 설치됨 → 카카오톡 앱으로 로그인
 * - 카카오톡 없음   → 카카오 계정(웹뷰) 로그인
 */
class KakaoAuthDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : SocialAuthDataSource {

    override suspend fun login(activity: Activity): Result<String> =
        suspendCancellableCoroutine { cont ->

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                when {
                    error != null -> {
                        cont.resume(
                            Result.failure(
                                AppError.AuthError.Unknown("카카오 로그인 실패: ${error.message}")
                            )
                        )
                    }
                    token != null -> {
                        cont.resume(Result.success(token.accessToken))
                    }
                    else -> {
                        cont.resume(
                            Result.failure(AppError.AuthError.Unknown("카카오 토큰 없음"))
                        )
                    }
                }
            }

            // 카카오톡 설치 여부에 따라 로그인 방법 분기
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(
                    context = activity,
                    callback = callback
                )
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    context = activity,
                    callback = callback
                )
            }
        }

    override suspend fun logout() {
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.logout { _ ->
                // 카카오 로그아웃은 항상 성공 처리 (SDK 스펙)
                cont.resume(Unit)
            }
        }
    }

    /** Kakao: SDK API로 프로필 조회 */
    suspend fun fetchKakaoProfile(): User =
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.me { user, error ->
                when {
                    error != null -> throw AppError.AuthError.Unknown("카카오 프로필 조회 실패: ${error.message}")
                    user != null -> cont.resume(
                        User(
                            uid = user.id?.toString() ?: "",
                            email = user.kakaoAccount?.email ?: "",
                            displayName = user.kakaoAccount?.profile?.nickname ?: "",
                            photoUrl = user.kakaoAccount?.profile?.thumbnailImageUrl,
                            loginType = SocialLoginType.KAKAO
                        )
                    )
                    else -> throw AppError.AuthError.Unknown("카카오 사용자 정보 없음")
                }
            }
        }
}