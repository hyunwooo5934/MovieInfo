package com.example.movieinfoex.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieinfoex.feature.login.ui.LoginRoute
import com.example.movieinfoex.feature.login.ui.LoginScreen
import com.example.movieinfoex.feature.main.MainRoute
import com.example.movieinfoex.feature.splash.ui.SplashRoute
import com.example.movieinfoex.feature.splash.ui.SplashScreen


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SPLASH.route
    ) {
        composable(route = Screen.SPLASH.route) {
            SplashRoute(
                onNavigateLogin = {
                    navController.navigate(Screen.LOGIN.route) {
                        popUpTo(Screen.LOGIN.route) { inclusive = true }
                    }
                },
                onNavigateHome = {
//                    navController.navigate(Screen.LOGIN.route) {
//                        popUpTo(Screen.LOGIN.route) { inclusive = true }
//                    }
                }
            )
        }

        composable(route = Screen.LOGIN.route) {
            LoginRoute(
                onNavigateHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Main.route) {
            MainRoute()
        }


    }


}