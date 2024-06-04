package com.learning.pestifyapp.ui.screen.navigation


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.di.factory.HomeFactory
import com.learning.pestifyapp.di.factory.ViewModelFactory
import com.learning.pestifyapp.ui.screen.authentication.forgotpassword.ForgotPasswordScreen
import com.learning.pestifyapp.ui.screen.authentication.forgotpassword.ForgotPasswordScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.login.LoginScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.login.LoginScreen
import com.learning.pestifyapp.ui.screen.authentication.register.RegisterScreen
import com.learning.pestifyapp.ui.screen.authentication.register.RegisterScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.username.UsernameScreen
import com.learning.pestifyapp.ui.screen.authentication.username.UsernameScreenViewModel
import com.learning.pestifyapp.ui.screen.dashboard.detail.DetailScreen
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaScreen
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryScreen
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.CameraScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PescanScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PescanScreenViewModel
import com.learning.pestifyapp.ui.screen.dashboard.profile.ProfileScreen
import com.learning.pestifyapp.ui.screen.onboarding.OnboardingScreen
import com.learning.pestifyapp.ui.screen.splashscreen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    context: MainActivity,
) {
    val userRepository = UserRepository(context)

    NavHost(
        navController = navController,
        startDestination = Graph.SPLASH,
        modifier = Modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {

        composable(route = Graph.SPLASH) {
            SplashScreen(
                navController = navController,
                context = context,
            )
        }

        composable(route = Graph.ONBOARDING) {
            OnboardingScreen(
                navController = navController,
                context = context
            )
        }

        // |||||||||||||||||||||=== LOGIN/REGISTER ===||||||||||||||||||||||||||||||||||

        composable(route = Graph.LOGIN) {
            val loginScreenViewModel: LoginScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))

            LoginScreen(
                navController = navController,
                context = context,
                viewModel = loginScreenViewModel
            )
        }
        composable(route = Graph.REGISTER) {
            val registerScreenViewModel: RegisterScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))

            RegisterScreen(
                navController = navController,
                context = context,
                viewModel = registerScreenViewModel
            )
        }
        composable(route = Graph.USERNAME) {
            val usernameViewModel: UsernameScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))
            UsernameScreen(
                navController = navController,
                context = context,
                viewModel = usernameViewModel
            )
        }
        composable(route = Graph.FORGOT_PASSWORD) {
            val forgotPasswordScreenViewModel: ForgotPasswordScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))
            ForgotPasswordScreen(
                navController = navController,
                context = context,
                viewModel = forgotPasswordScreenViewModel
            )
        }

        // |||||||||||||||||||||=== SCAN ===||||||||||||||||||||||||||||||||||

        composable(route = Graph.CAMERA) {
            val pescanScreenViewModel: PescanScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))
            CameraScreen(
                navController = navController,
                context = context,
                viewModel = pescanScreenViewModel
            )
        }

        // |||||||||||||||||||||=== HOME DASHBOARD ===||||||||||||||||||||||||||||||||||

        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
                context = context,
                viewModel = viewModel(factory = HomeFactory.getInstance())
            )
        }

        composable(route = Screen.Ensiklopedia.route) {
            EnsiklopediaScreen(
            )
        }

        composable(route = Screen.Pescan.route) {
            val pescanScreenViewModel: PescanScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))
            PescanScreen(
                navController = navController,
                context = context,
                viewModel = pescanScreenViewModel
            )
        }

        composable(route = Screen.History.route) {
            HistoryScreen(
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
            )
        }

        // |||||||||||||||||||||=== DETAIL ===||||||||||||||||||||||||||||||||||

        composable(
            route = Screen.DetailPlant.route,
            arguments = listOf(
                navArgument("plantId") {
                    type = NavType.LongType
                }
            )
        ) {
            val id = it.arguments?.getLong("plantId") ?: -1L
            DetailScreen(
                plantId = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

//        composable(route = Graph.DASHBOARD) {
//            dashBoard(
//                navController = navController,
//                context = context,
//            )
//        }

    }
}