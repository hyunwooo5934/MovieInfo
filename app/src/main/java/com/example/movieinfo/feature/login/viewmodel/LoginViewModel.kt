package com.example.movieinfo.feature.login.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.GoogleAuthDataSource
import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.toAppError
import com.example.domain.usecase.SocialLoginUseCase
import com.example.movieinfo.feature.login.model.LoginEffect
import com.example.movieinfo.feature.login.model.LoginIntent
import com.example.movieinfo.feature.login.model.LoginState
import com.example.snslogin.data.datasource.NaverAuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socialLoginUseCase: SocialLoginUseCase,
    private val googleAuthDataSource: GoogleAuthDataSource,
    private val naverAuthDataSource: NaverAuthDataSource
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()



    fun onIntent(intent: LoginIntent) {
        when(intent) {
            is LoginIntent.SocialLogin -> startSocialLogin(intent.type, intent.activity)
        }
    }


    // ── 소셜 로그인 ─────────────────────────────────────────────────

    private fun startSocialLogin(type: SocialLoginType, activity: Activity) {
        viewModelScope.launch {
            _state.value = LoginState.Loading(type)

            // Step 1: 소셜 SDK에서 토큰 획득 → Result<String>
            val tokenResult: Result<String> = getTokenFromDataSource(type, activity)

            // Step 2: 토큰 → UseCase → Repository → Result<User>
            // mapCatching: 토큰 획득 성공 시 UseCase 호출, 실패 시 그대로 전파
            tokenResult
                .mapCatching { token ->
                    socialLoginUseCase(token, type).getOrThrow()
                }
                .fold(
                    onSuccess = { user ->
                        _state.value = LoginState.Success(user)
                        _effect.emit(LoginEffect.NavigateToMain)
                    },
                    onFailure = { throwable ->
                        handleLoginFailure(throwable)
                    }
                )
        }
    }

    /**
     * 로그인 타입에 따라 DataSource 분기
     * 모든 DataSource는 Result<String> 반환 → AppError로 에러 타입 통일
     */
    private suspend fun getTokenFromDataSource(
        type: SocialLoginType,
        activity: Activity
    ): Result<String> = when (type) {
        SocialLoginType.GOOGLE -> googleAuthDataSource.login(activity)
        SocialLoginType.NAVER  -> naverAuthDataSource.login(activity) // 추후 변경
        SocialLoginType.KAKAO  -> googleAuthDataSource.login(activity) // 추후 변경
    }


    /**
     * 로그인 실패 처리
     * - UserCancelled: 사용자 직접 취소 → Idle 상태로 복귀, 에러 메시지 없음
     * - 그 외: Failure 상태로 변경 + ShowError Effect 발행
     */
    private suspend fun handleLoginFailure(throwable: Throwable) {
        when (val appError = throwable.toAppError()) {
            is AppError.AuthError.UserCancelled -> {
                // 사용자가 직접 창을 닫은 경우 → 에러 표시 없이 Idle로 복귀
                _state.value = LoginState.Idle
            }
            else -> {
                _state.value = LoginState.Failure(appError)
                _effect.emit(LoginEffect.ShowError(appError.message))
            }
        }
    }

}