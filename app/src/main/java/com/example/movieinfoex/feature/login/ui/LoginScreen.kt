package com.example.movieinfoex.feature.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movieinfo.R
import com.example.movieinfoex.feature.login.model.LoginState
import com.example.movieinfoex.feature.login.ui.component.BottomTermsSection
import com.example.movieinfoex.feature.login.ui.component.OliveLogoSection
import com.example.movieinfoex.feature.login.ui.component.SocialOutlinedButton

@OptIn(ExperimentalMaterial3Api::class)

//@androidx.compose.ui.tooling.preview.Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun OliveLoginScreenPreview() {
//    MaterialTheme {
//        LoginScreen()
//    }
//}


@Composable
fun LoginScreen(
    state: LoginState,
    onGoogleClick: () -> Unit = {},
    onNaverClick: () -> Unit = {},
    onKakaoClick: () -> Unit = {}
){

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF3F3F3)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(200.dp))

            OliveLogoSection()

            Spacer(modifier = Modifier.height(30.dp))


            SocialOutlinedButton(
                text = stringResource(R.string.login_google_s_text),
                onClick = onGoogleClick,
                iconRes = R.drawable.icon_google,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            SocialOutlinedButton(
                text = stringResource(R.string.login_naver_s_text),
                onClick = onNaverClick,
                iconRes = R.drawable.icon_naver,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(10.dp))

            SocialOutlinedButton(
                text = stringResource(R.string.login_kakao_s_text),
                onClick = onKakaoClick,
                iconRes = R.drawable.icon_kakao,
                color = Color.Yellow
            )

            Spacer(modifier = Modifier.weight(1f))

            BottomTermsSection()

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}



