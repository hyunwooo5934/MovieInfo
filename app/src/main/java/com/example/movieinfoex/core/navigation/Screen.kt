package com.example.movieinfoex.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (
    var title: String,
    var route: String,
    var icon: ImageVector?
) {

    // App
    data object SPLASH :
        Screen(NavigationType.SPLASH,NavigationType.SPLASH, null)
    data object LOGIN :
        Screen(NavigationType.LOGIN,NavigationType.LOGIN, null)
    data object Main :
        Screen(NavigationType.MAIN,NavigationType.MAIN, null)


    // Main
    data object Home :
        Screen(NavigationType.HOME,NavigationType.HOME, null)
    data object MyInfo :
        Screen(NavigationType.MYINFO,NavigationType.MYINFO, null)

}

class NavigationType {
    companion object {
        // App Route
        const val SPLASH = "splash"
        const val LOGIN = "login"
        const val MAIN = "Main"


        // Main
        const val HOME = "home"
        const val MYINFO = "MyInfo"
    }
}