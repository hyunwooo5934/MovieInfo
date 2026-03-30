package com.example.movieinfo.feature.login.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.SocialLoginType
import com.example.movieinfo.feature.login.model.LoginEffect
import com.example.movieinfo.feature.login.model.LoginIntent
import com.example.movieinfo.feature.login.viewmodel.LoginViewModel

@Composable
fun LoginRoute(
    onNavigateMain: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToMain ->  onNavigateMain()
                is LoginEffect.ShowError -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    LoginScreen(
        state,
        onGoogleClick = { viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.GOOGLE,context as Activity)) },
        onNaverClick = { viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.NAVER,context as Activity)) },
        onKakaoClick = { viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.GOOGLE,context as Activity)) }
    )


}