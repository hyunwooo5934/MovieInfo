package com.example.movieinfo.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieinfo.feature.login.ui.LoginRoute
import com.example.movieinfo.feature.main.MainScreen
import com.example.movieinfo.feature.splash.ui.SplashRoute


@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SPLASH.route
    ) {
        composable(route = Screen.SPLASH.route) {
            SplashRoute(
                onNavigateLogin = {
                    navController.navigate(Screen.LOGIN.route) {
                        popUpTo(Screen.SPLASH.route) { inclusive = true }
                    }
                },
                onNavigateMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.SPLASH.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.LOGIN.route) {
            LoginRoute(
                onNavigateMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.LOGIN.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Main.route) {
            MainScreen()
        }
    }


}