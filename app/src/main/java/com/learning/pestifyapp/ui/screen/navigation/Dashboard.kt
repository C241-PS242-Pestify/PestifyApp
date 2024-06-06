package com.learning.pestifyapp.ui.screen.navigation


import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.di.factory.HomeFactory
import com.learning.pestifyapp.di.factory.ViewModelFactory
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaScreen
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryScreen
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PescanScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PescanScreenViewModel
import com.learning.pestifyapp.ui.screen.dashboard.profile.ProfileScreen

fun NavGraphBuilder.dashBoard(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    context: MainActivity,
    ) {
    val userRepository = UserRepository(context)

    navigation(
        startDestination = Screen.Home.route,
        route = Graph.DASHBOARD,
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                context = context,
                viewModel = viewModel(factory = HomeFactory.getInstance())
            )
        }

        composable(Screen.Ensiklopedia.route) {
            EnsiklopediaScreen(
            )
        }

        composable(Screen.Pescan.route) {
            val pescanScreenViewModel: PescanScreenViewModel = viewModel(factory = ViewModelFactory(userRepository))
            PescanScreen(
                navController = navController,
                context = context,
                viewModel = pescanScreenViewModel
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