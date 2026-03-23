package com.example.movieinfoex.feature.main.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(
        route = "home",
        icon = Icons.Default.Home,
        label = "홈"
    )

    object MyInfo : BottomNavItem(
        route = "myinfo",
        icon = Icons.Default.Person,
        label = "나의 무비"
    )

    object Settings : BottomNavItem(
        route = "settings",
        icon = Icons.Default.Settings,
        label = "설정"
    )
}