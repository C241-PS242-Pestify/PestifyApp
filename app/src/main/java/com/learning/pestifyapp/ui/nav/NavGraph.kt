package com.learning.pestifyapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.learning.pestifyapp.ui.screen.*
import com.learning.pestifyapp.ui.viewmodel.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {
    val authViewModel: AuthViewModel = viewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Home.route else Screen.OnBoarding.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {  }
        composable(Screen.OnBoarding.route) { OnBoardingScreen(navController = navController) }
        composable(Screen.Login.route) { LoginScreen(authViewModel = authViewModel) }
        composable(Screen.Register.route) { RegisterScreen() }
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Insight.route) { InsightScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
        composable(Screen.Scan.route) { ScanScreen() }
        composable(Screen.History.route) { HistoryScreen() }
    }
}
