package com.example.data.datasource

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.data.di.GoogleWebClientId
import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import javax.inject.Inject


/**
 * Google 로그인 DataSource
 *
 * Credential Manager API 사용 (최신 Google Sign-In 방식)
 * - 구버전 GoogleSignInClient 대체
 * - One Tap 지원
 *
 * 반환: idToken (JWT)
 * → 추후 서버 검증 시 이 idToken을 서버로 전달
 */
class GoogleAuthDataSource @Inject constructor(
    @field:GoogleWebClientId private val webClientId: String,
    @field:ApplicationContext private val context: Context
) : SocialAuthDataSource {


    override suspend fun login(activity: Activity): Result<String> = try {
        val credentialManager = CredentialManager.create(activity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // 전체 계정 표시
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)          // 자동 선택 비활성화
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = activity
        )

        val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
        Result.success(credential.idToken)

    } catch (e: CancellationException) {
        throw e // 코루틴 취소는 반드시 재전파
    } catch (e: GetCredentialCancellationException) {
        // 사용자가 직접 창을 닫은 경우 → 에러 메시지 표시 안 함
        Result.failure(AppError.AuthError.UserCancelled)
    } catch (e: GetCredentialException) {
        Result.failure(AppError.AuthError.Unknown(e.message ?: ""))
    } catch (e: Exception) {
        Result.failure(AppError.AuthError.Unknown(e.message ?: ""))
    }

    /**
     * 자동 로그인용: 기존 저장된 Google 계정 자동 조회
     * setFilterByAuthorizedAccounts(true) + setAutoSelectEnabled(true) 조합
     */
    suspend fun getSignedInUser(): User? = try {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true) // 기존 로그인 계정만
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)          // 자동 선택 (UI 없음)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context,request)
        val credential = GoogleIdTokenCredential.createFrom(result.credential.data)

        User(
            uid          = credential.id,
            email        = credential.id,
            displayName  = credential.displayName ?: "",
            photoUrl     = credential.profilePictureUri?.toString(),
            loginType    = SocialLoginType.GOOGLE
        )
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        null // 저장된 계정 없거나 토큰 만료
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }


}