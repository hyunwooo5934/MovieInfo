package com.example.movieinfo.feature.splash.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.movieinfo.feature.splash.model.SplashEffect
import com.example.movieinfo.feature.splash.model.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Idle)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SplashEffect>(replay = 1)
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()


    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            delay(1000)
            val user = getCurrentUserUseCase()
            if (user != null) {
                _state.value = SplashState.Success(user)
                _effect.emit(SplashEffect.NavigateMain)
            } else {
                _effect.emit(SplashEffect.NavigateLogin)
            }
            // user == null → 상태 변경 없음 → Splash에서 Login으로 이동
        }
    }




}