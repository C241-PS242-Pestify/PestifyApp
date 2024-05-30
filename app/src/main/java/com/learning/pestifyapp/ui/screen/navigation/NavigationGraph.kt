package com.learning.pestifyapp.ui.screen.navigation


import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.learning.pestifyapp.ui.screen.authentication.forgotpassword.ForgotPasswordScreen
import com.learning.pestifyapp.ui.screen.authentication.forgotpassword.ForgotPasswordScreenViewModel
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
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        }
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
        composable(route = Graph.FORGOT_PASSWORD) {
            val forgotPasswordScreenViewModel: ForgotPasswordScreenViewModel = viewModel(factory = ViewModelFactory(userRepository))
            ForgotPasswordScreen(
                navController = navController,
                context = context,
                viewModel = forgotPasswordScreenViewModel
            )
        }

        dashBoard(
            navController = navController,
        )

    }
}