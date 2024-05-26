package com.learning.pestifyapp.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNav(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Insight,
        Screen.Profile,
        Screen.Scan,
        Screen.History
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        is Screen.Home -> Icon(Icons.Filled.Home, contentDescription = screen.route)
                        is Screen.Insight -> Icon(Icons.Filled.Face, contentDescription = screen.route)
                        is Screen.Profile -> Icon(Icons.Filled.AccountBox, contentDescription = screen.route)
                        is Screen.Scan -> Icon(Icons.Filled.Search, contentDescription = screen.route)
                        is Screen.History -> Icon(Icons.Filled.Add, contentDescription = screen.route)
                        Screen.Login -> TODO()
                        Screen.OnBoarding -> TODO()
                        Screen.Register -> TODO()
                        Screen.Splash -> TODO()
                    }
                },
                label = { Text(screen.route) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
