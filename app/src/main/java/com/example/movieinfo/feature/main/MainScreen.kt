package com.example.movieinfo.feature.main

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.movieinfo.feature.tab.navigation.TabRoute

@Composable
fun MainScreen() {
    Log.d("MainScreen", "MainScreen Composable")
    TabRoute()
}