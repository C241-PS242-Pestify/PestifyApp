package com.learning.pestifyapp.ui.screen.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.UserRepository
import com.learning.pestifyapp.ui.screen.ViewModelFactory
import com.learning.pestifyapp.ui.screen.authentication.login.LoginScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.login.LoginScreen
import com.learning.pestifyapp.ui.screen.authentication.register.RegisterScreen
import com.learning.pestifyapp.ui.screen.authentication.register.RegisterScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.username.UsernameScreen
import com.learning.pestifyapp.ui.screen.authentication.username.UsernameScreenViewModel
import com.learning.pestifyapp.ui.screen.onboarding.OnboardingScreen
import com.learning.pestifyapp.ui.screen.splashscreen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    val userRepository = UserRepository(context)

    NavHost(
        navController = navController,
        startDestination = Graph.SPLASH,
        modifier = Modifier.padding(innerPadding),
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
        composable(route = Graph.LOGIN) {
            val loginScreenViewModel: LoginScreenViewModel = viewModel(factory = ViewModelFactory(userRepository))

            LoginScreen(
                navController = navController,
                context = context,
                viewModel = loginScreenViewModel
            )
        }
        composable(route = Graph.REGISTER) {
            val registerScreenViewModel: RegisterScreenViewModel = viewModel(factory = ViewModelFactory(userRepository))

            RegisterScreen(
                navController = navController,
                context = context,
                viewModel = registerScreenViewModel
            )
        }
        composable(route = Graph.USERNAME) {
            val usernameViewModel: UsernameScreenViewModel = viewModel(factory = ViewModelFactory(userRepository))
            UsernameScreen(
                navController = navController,
                context = context,
                viewModel = usernameViewModel
            )
        }

        dashBoard(
            navController = navController,
        )

    }
}