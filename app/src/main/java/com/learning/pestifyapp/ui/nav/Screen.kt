package com.learning.pestifyapp.ui.nav

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Insight : Screen("insight")
    object Scan : Screen("scan")
    object History : Screen("history")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object Splash : Screen("splash")
    object OnBoarding : Screen("onboarding")

    companion object {
        fun fromRoute(route: String): Screen = when (route) {
            "home" -> Home
            "insight" -> Insight
            "scan" -> Scan
            "history" -> History
            "profile" -> Profile
            "login" -> Login
            "register" -> Register
            "splash" -> Splash
            "onboarding" -> OnBoarding
            else -> throw IllegalArgumentException("Route $route is not recognized.")
        }
    }
}
