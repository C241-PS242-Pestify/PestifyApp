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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.ui.screen.navigation.Graph
import com.learning.pestifyapp.ui.screen.navigation.Screen
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

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ss_logo))
    var isAnimationFinished by remember { mutableStateOf(false) }

    LaunchedEffect(composition) {
        if (composition != null) {
            alpha.animateTo(
                1f,
                animationSpec = tween(2500)
            )
            delay(1000)
            isAnimationFinished = true
        }
    }

    LaunchedEffect(isAnimationFinished) {
        if (isAnimationFinished) {
            val userRepository = UserRepository(context)
            if (userRepository.getLoginStatus()) {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            } else {
                navController.popBackStack()
                navController.navigate(Graph.ONBOARDING)
            }
        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (composition != null) {
            LoaderAnimation(
                modifier = Modifier.size(250.dp), anim = R.raw.ss_logo
            )
        }
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(anim))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier
    )
}

private fun onBoardingIsFinished(context: MainActivity): Boolean {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("Finished", false)
}
