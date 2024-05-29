package com.learning.pestifyapp.ui.screen.navigation


import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.learning.pestifyapp.data.UserRepository
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaScreen
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryScreen
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeScreen
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeScreenViewModel
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PescanScreen
import com.learning.pestifyapp.ui.screen.dashboard.profile.ProfileScreen

fun NavGraphBuilder.dashBoard(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation(
        startDestination = Screen.Home.route,
        route = Graph.DASHBOARD,
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = HomeScreenViewModel(userRepository = UserRepository(context = LocalContext.current))
            )
        }

        composable(Screen.Ensiklopedia.route) {
            EnsiklopediaScreen(
            )
        }

        composable(Screen.Pescan.route) {
            PescanScreen(
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
            )
        }
    }
}