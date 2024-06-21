package com.learning.pestifyapp.ui.screen.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Ensiklopedia : Screen("pespedia")
    object Pescan : Screen("pescan")
    object History : Screen("bookmark")
    object Profile : Screen("profile")

    object DetailPlant : Screen("detailArt/{plantId}") {
        fun createRoute(plantId: String) = "detailArt/$plantId"
    }

    object DetailEns : Screen("detailEns/{ensId}") {
        fun createRoute(ensId: String) = "detailEns/$ensId"
    }

    object DetailArticle : Screen("detailArticle/{articleId}") {
        fun createRoute(articleId: String) = "detailArticle/$articleId"
    }

    object DetailCategories : Screen("detailCategories/{category}") {
        fun createRoute(category: String) = "detailCategories/$category"
    }
}

object Graph {
    const val DASHBOARD = "dashboard"
    const val SPLASH = "splashscreen"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val USERNAME = "username"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot-password"
    const val CAMERA = "camera"
    const val EDIT_PROFILE = "edit-profile"
    const val CHANGE_PASSWORD = "change-password"
}