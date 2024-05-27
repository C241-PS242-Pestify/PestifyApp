package com.learning.pestifyapp.ui.screen.navigation

import com.learning.pestifyapp.R

data class NavigationItem(
    val title: String,
    val icon: Int,
    val iconSelected: Int,
    val screen: Screen
)

val navigationItems = listOf(
    NavigationItem(
        title = Screen.Home.route,
        icon = R.drawable.home_non,
        iconSelected = R.drawable.home_filled,
        screen = Screen.Home
    ),
    NavigationItem(
        title = Screen.Ensiklopedia.route,
        icon = R.drawable.book_non,
        iconSelected = R.drawable.book_filled,
        screen = Screen.Ensiklopedia
    ),
    NavigationItem(
        title = Screen.Pescan.route,
        icon = R.drawable.scan,
        iconSelected = R.drawable.scan,
        screen = Screen.Pescan
    ),
    NavigationItem(
        title = Screen.History.route,
        icon = R.drawable.history_non,
        iconSelected = R.drawable.history_filled,
        screen = Screen.History
    ),
    NavigationItem(
        title = Screen.Profile.route,
        icon = R.drawable.account_non,
        iconSelected = R.drawable.account_filled,
        screen = Screen.Profile
    ),
)