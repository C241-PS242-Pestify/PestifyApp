package com.learning.pestifyapp.ui.screen.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Ensiklopedia : Screen("esiklopedia")
    object Pescan : Screen("pescan")
    object History : Screen("history")
    object Profile : Screen("profile")
}

object Graph {
    const val DASHBOARD = "dashboard"
    const val SPLASH = "splashscreen"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val USERNAME = "username"
    const val FORGOT_PASSWORD = "forgot-password"
    const val CAMERA = "camera"



}