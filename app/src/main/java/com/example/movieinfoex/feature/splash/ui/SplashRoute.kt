package com.example.movieinfoex.feature.splash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieinfoex.feature.splash.model.SplashIntent
import com.example.movieinfoex.feature.splash.viewmodel.SplashViewModel

@Composable
fun SplashRoute(
    onNavigateLogin: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(SplashIntent.CheckAutoLogin)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect -> onNavigateLogin()
//            when (effect) {
//                SplashEffect.NavigateLogin -> onNavigateLogin()
//                SplashEffect.NavigateHome -> onNavigateHome()
//            }
        }
    }

    SplashScreen(state = state)

}