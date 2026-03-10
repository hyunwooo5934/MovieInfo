package com.example.movieinfoex.feature.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieinfoex.feature.splash.model.SplashEffect
import com.example.movieinfoex.feature.splash.model.SplashIntent
import com.example.movieinfoex.feature.splash.model.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()


    fun onIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.CheckAutoLogin -> checkAutoLogin()
        }
    }

    private fun checkAutoLogin() { // 추후 작업 진행예정
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(3000)
            _effect.emit(SplashEffect.NavigateLogin)
            _state.update { it.copy(isLoading = false) }
        }
    }





}