package com.learning.pestifyapp.ui.screen.splashscreen

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.screen.navigation.Graph
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {

    val alpha = remember {
        Animatable(0f)
    }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.ss_logo))
    val isAnimationLoaded = composition != null

    LaunchedEffect(isAnimationLoaded) {
        if (isAnimationLoaded) {
            alpha.animateTo(
                1f,
                animationSpec = tween(2500)
            )
            delay(1000)
            navController.popBackStack()
            navController.navigate(Graph.DASHBOARD)

            // Uncomment and adjust this section if you have user login logic
            // val userRepository = UserRepository(context)
            // if (userRepository.isLoggedIn()) {
            //     navController.popBackStack()
            //     navController.navigate(Graph.DASHBOARD)
            // } else {
            //     navController.popBackStack()
            //     navController.navigate(Graph.ONBOARDING)
            // }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isAnimationLoaded) {
            LoaderAnimation(
                modifier = Modifier.size(250.dp), anim = R.raw.ss_logo
            )
        }
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anim))

    LottieAnimation(
        composition = composition, iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}

private fun onBoardingIsFinished(context: MainActivity): Boolean {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("Finished", false)
}
