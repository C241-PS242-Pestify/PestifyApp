package com.learning.pestifyapp.ui.screen.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Ensiklopedia : Screen("Ensiklopedia")
    object Pescan : Screen("Pescan")
    object History : Screen("History")
    object Profile : Screen("Profile")
}

object Graph {
    const val DASHBOARD = "dashboard"
    const val SPLASH = "splashscreen"
    const val ONBOARDING = "onboarding"
}