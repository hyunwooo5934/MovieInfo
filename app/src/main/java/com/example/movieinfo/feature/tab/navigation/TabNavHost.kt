package com.example.movieinfo.feature.tab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movieinfo.core.navigation.MainTabRoute
import com.example.movieinfo.feature.favorite.ui.FavoriteScreen
import com.example.movieinfo.feature.home.ui.HomeScreen
import com.example.movieinfo.feature.mypage.ui.MyPageScreen

@Composable
fun TabNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainTabRoute.Home.route
    ) {
        composable(MainTabRoute.Home.route) {
            HomeScreen()
        }
        composable(MainTabRoute.Favorite.route) {
            FavoriteScreen()
        }
        composable(MainTabRoute.MyPage.route) {
            MyPageScreen()
        }
    }
}
