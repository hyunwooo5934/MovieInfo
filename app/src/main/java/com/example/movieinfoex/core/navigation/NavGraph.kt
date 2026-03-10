package com.example.movieinfoex.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieinfoex.feature.login.ui.LoginScreen
import com.example.movieinfoex.feature.splash.ui.SplashRoute
import com.example.movieinfoex.feature.splash.ui.SplashScreen


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
            LoginScreen(navHostController = navController)
        }

//        composable(route = Screen.Home.route) {
//            LoginScreen(navHostController = navController)
//        }
    }


}