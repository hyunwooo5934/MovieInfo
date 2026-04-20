package com.example.movieinfo.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainTabRoute(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : MainTabRoute("home", "홈", Icons.Filled.Home)
    data object Search : MainTabRoute("search", "검색", Icons.Filled.Search)
    data object Favorite : MainTabRoute("favorite", "즐겨찾기", Icons.Filled.Favorite)
    data object MyPage : MainTabRoute("mypage", "마이", Icons.Filled.Person)
}
