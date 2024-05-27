package com.learning.pestifyapp.ui.screen.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.ui.screen.onboarding.OnboardingScreen
import com.learning.pestifyapp.ui.screen.splashscreen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    context: MainActivity,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Graph.SPLASH,
        modifier = Modifier.padding(innerPadding)
        ,
        enterTransition = {EnterTransition.None},
        exitTransition = {ExitTransition.None},
        popEnterTransition = {EnterTransition.None},
        popExitTransition = {ExitTransition.None}
    ){

        composable(route = Graph.SPLASH) {
            SplashScreen(
                navController = navController,
                context = context
            )
        }

        composable(route = Graph.ONBOARDING) {
            OnboardingScreen(
                navController = navController,
                context = context
            )
        }

        dashBoard(
            navController = navController,
        )

    }
}