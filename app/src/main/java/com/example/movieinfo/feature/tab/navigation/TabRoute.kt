package com.example.movieinfo.feature.tab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movieinfo.feature.tab.ui.TabScreen

@Composable
fun TabRoute() {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    TabScreen(
        currentTabRoute = currentRoute,
        onTabClick = { route ->
            tabNavController.navigate(route) {
                popUpTo(tabNavController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        content = {
            TabNavHost(navController = tabNavController)
        }
    )
}
