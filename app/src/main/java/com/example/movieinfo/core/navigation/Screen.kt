package com.example.movieinfo.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (
    var title: String,
    var route: String,
    var icon: ImageVector?
) {

    data object SPLASH :
        Screen(NavigationType.SPLASH,NavigationType.SPLASH, null)

    data object LOGIN :
        Screen(NavigationType.LOGIN,NavigationType.LOGIN, null)

    data object Main :
        Screen(NavigationType.Main,NavigationType.Main, null)

}

class NavigationType {
    companion object {
        const val SPLASH = "splash"
        const val LOGIN = "login"
        const val Main = "main"
    }
}