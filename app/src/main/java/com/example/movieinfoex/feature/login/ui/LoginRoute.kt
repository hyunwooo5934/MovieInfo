package com.example.movieinfoex.feature.login.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieinfoex.feature.login.model.LoginEffect
import com.example.movieinfoex.feature.login.model.LoginIntent
import com.example.movieinfoex.feature.login.viewmodel.LoginViewModel

@Composable
fun LoginRoute(
    onNavigateHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(LoginIntent.CheckAutoLogin)
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                LoginEffect.NavigateHome -> onNavigateHome()
            }
        }
    }

    LoginScreen(
        state,
        onGoogleClick = { viewModel.onIntent(LoginIntent.ClickGoogle) },
        onNaverClick = { viewModel.onIntent(LoginIntent.ClickNaver) },
        onKakaoClick = { viewModel.onIntent(LoginIntent.ClickKakao) }
    )


}