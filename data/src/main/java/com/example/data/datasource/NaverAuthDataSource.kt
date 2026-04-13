package com.example.snslogin.data.datasource

import android.app.Activity
import android.util.Log
import com.example.data.datasource.SocialAuthDataSource
import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.oauth.util.NidOAuthCallback
import com.navercorp.nid.profile.domain.vo.NidProfile
import com.navercorp.nid.profile.util.NidProfileCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Naver 로그인 DataSource
 *
 * Naver OAuth SDK 사용
 * 반환: accessToken
 * → 추후 서버 검증 시 이 accessToken을 서버로 전달
 *
 * 콜백 기반 SDK를 suspendCancellableCoroutine으로 코루틴화
 */
class NaverAuthDataSource : SocialAuthDataSource {

    override suspend fun login(activity: Activity): Result<String> =
        suspendCancellableCoroutine { cont ->
            Log.e("NaverAuthDataSource","login");
            val callback = object : NidOAuthCallback {
                override fun onSuccess() {
                    val token = NidOAuth.getAccessToken()
                    if (token != null) {
                        cont.resume(Result.success(token))
                    } else {
                        cont.resume(
                            Result.failure(
                                AppError.AuthError.Unknown("네이버 토큰 없음")
                            )
                        )
                    }
                }

                override fun onFailure(errorCode: String, errorDesc: String) {
                    cont.resume(
                        Result.failure(
                            AppError.AuthError.Unknown("네이버 로그인 실패: [$errorCode] $errorDesc")
                        )
                    )
                }
            }

            NidOAuth.requestLogin(activity, callback)
            cont.invokeOnCancellation {
                // 취소 시 추가 정리 작업이 필요하면 여기에 작성
            }
        }


    override suspend fun logout(): Unit = suspendCancellableCoroutine { cont ->
        val callback = object : NidOAuthCallback {
            override fun onSuccess() {
                // 클라이언트 토큰 삭제 완료
                cont.resume(Unit)
            }
            override fun onFailure(errorCode: String, errorDesc: String) {
                // 실패해도 UI 처리는 상위에서 결정 → resume으로 정상 반환
                cont.resume(Unit)
            }
        }
        NidOAuth.logout(callback)
    }


    /** Naver: SDK API로 프로필 조회 */
    suspend fun fetchNaverProfile(): User =
        suspendCancellableCoroutine { cont ->
            NidOAuth.getUserProfile(
                object : NidProfileCallback<NidProfile> {
                    override fun onSuccess(result: NidProfile) {
                        if (!cont.isActive) return
                        cont.resume(
                            User(
                                uid = result.profile.id.orEmpty(),
                                email = result.profile.email.orEmpty(),
                                displayName = result.profile.name.orEmpty(),
                                photoUrl = result.profile.profileImage,
                                loginType = SocialLoginType.NAVER
                            )
                        )
                    }

                    override fun onFailure(errorCode: String, errorDesc: String) {
                        cont.resumeWithException(
                            AppError.AuthError.Unknown(
                                "네이버 프로필 조회 실패: $errorCode / $errorDesc"
                            )
                        )
                    }
                }
            )
        }
}
