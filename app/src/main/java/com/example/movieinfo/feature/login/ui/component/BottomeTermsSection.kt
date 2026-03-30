package com.example.movieinfo.feature.login.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieinfo.R

@Composable
fun BottomTermsSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = stringResource(R.string.login_bottom_text1),
                color = Color(0xFFB3B3B3),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.login_bottom_text2),
                color = Color(0xFF9D9D9D),
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.login_bottom_text3),
            color = Color(0xFFBDBDBD),
            fontSize = 12.sp
        )
    }
}