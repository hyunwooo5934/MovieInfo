package com.example.movieinfo.feature.splash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieinfo.feature.splash.model.SplashEffect
import com.example.movieinfo.feature.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashRoute(
    onNavigateLogin: () -> Unit,
    onNavigateMain: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when(effect){
                is SplashEffect.NavigateMain -> {
                    onNavigateMain()
                }
                is SplashEffect.NavigateLogin -> {
                    onNavigateLogin()
                }
            }
        }
    }

    SplashScreen()

}