package com.learning.pestifyapp.ui.screen.navigation

import android.content.Context
import com.learning.pestifyapp.R

data class NavigationItem(
    val title: String,
    val icon: Int,
    val iconSelected: Int,
    val screen: Screen
)

fun getNavigationRoute(): List<String> {
    return listOf(
        Screen.Home.route,
        Screen.Ensiklopedia.route,
        Screen.Pescan.route,
        Screen.History.route,
        Screen.Profile.route
    )
}

fun getNavigationItems(context: Context): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = context.getString(R.string.menu_home),
            icon = R.drawable.home_non,
            iconSelected = R.drawable.home_filled,
            screen = Screen.Home
        ),
        NavigationItem(
            title = context.getString(R.string.menu_ens),
            icon = R.drawable.book_non,
            iconSelected = R.drawable.book_filled,
            screen = Screen.Ensiklopedia
        ),
        NavigationItem(
            title = context.getString(R.string.menu_pescan),
            icon = R.drawable.scan,
            iconSelected = R.drawable.scan,
            screen = Screen.Pescan
        ),
        NavigationItem(
            title = context.getString(R.string.menu_history),
            icon = R.drawable.history_non,
            iconSelected = R.drawable.history_filled,
            screen = Screen.History
        ),
        NavigationItem(
            title = context.getString(R.string.menu_profile),
            icon = R.drawable.account_non,
            iconSelected = R.drawable.account_filled,
            screen = Screen.Profile
        ),
    )
}