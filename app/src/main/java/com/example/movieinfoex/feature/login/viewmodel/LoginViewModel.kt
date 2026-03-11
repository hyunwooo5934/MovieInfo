package com.example.movieinfoex.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AuthError
import com.example.domain.model.AuthResult
import com.example.domain.model.SocialProvider
import com.example.domain.usecase.GetSavedUserUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.VerifySocialLoginUseCase
import com.example.movieinfoex.feature.login.model.LoginEffect
import com.example.movieinfoex.feature.login.model.LoginIntent
import com.example.movieinfoex.feature.login.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val verifySocialLoginUseCase: VerifySocialLoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.ClickGoogle -> login(SocialProvider.GOOGLE)
            LoginIntent.ClickKakao -> login(SocialProvider.KAKAO)
            LoginIntent.ClickNaver -> login(SocialProvider.NAVER)
            LoginIntent.CheckAutoLogin -> checkAutoLogin()
        }
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            val user = getSavedUserUseCase()
            if (user != null) {
                _state.update { it.copy(user = user, isLoginSuccess = true) }
                _effect.emit(LoginEffect.NavigateHome)
            }
        }
    }

    private fun login(provider: SocialProvider) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = loginUseCase(provider)) {
                is AuthResult.Success -> {
                    val verified = verifySocialLoginUseCase(result.credential)
                    if (verified) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                user = result.user,
                                isLoginSuccess = true
                            )
                        }
                        _effect.emit(LoginEffect.NavigateHome)
                    } else {
                        _state.update {
                            it.copy(isLoading = false, errorMessage = "검증 실패")
                        }
                        _effect.emit(LoginEffect.ShowToast("검증 실패"))
                    }
                }

                is AuthResult.Failure -> {
                    val message = result.error.toMessage()
                    _state.update {
                        it.copy(isLoading = false, errorMessage = message)
                    }
                    _effect.emit(LoginEffect.ShowToast(message))
                }
            }
        }
    }

    private fun AuthError.toMessage(): String = when (this) {
        AuthError.Cancelled -> "로그인이 취소되었습니다."
        AuthError.Network -> "네트워크 오류가 발생했습니다."
        AuthError.InvalidCredential -> "인증 정보가 올바르지 않습니다."
        AuthError.NotSupported -> "지원하지 않는 로그인 방식입니다."
        is AuthError.Unknown -> this.message ?: "알 수 없는 오류입니다."
    }


}