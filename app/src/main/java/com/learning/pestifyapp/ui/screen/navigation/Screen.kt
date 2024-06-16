package com.learning.pestifyapp.ui.screen.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Ensiklopedia : Screen("pespedia")
    object Pescan : Screen("pescan")
    object History : Screen("history")
    object Profile : Screen("profile")

    object DetailPlant : Screen("detail/{plantId}") {
        fun createRoute(plantId: Long) = "detail/$plantId"
    }

    object Username : Screen("username")
}

object Graph {
    const val DASHBOARD = "dashboard"
    const val SPLASH = "splashscreen"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot-password"
    const val CAMERA = "camera"
    const val PRIVACY = "privacy"




}