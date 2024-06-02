package com.learning.pestifyapp.ui.screen.onboarding

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.PestifyApp
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.components.CustomButton
import com.learning.pestifyapp.ui.screen.navigation.Graph
import com.learning.pestifyapp.ui.theme.PestifyAppTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    context: MainActivity
) {
    val animations = listOf(
        R.raw.lottie_onboarding_1,
        R.raw.lottie_onboarding_1,
        R.raw.lottie_onboarding_1
    )
    val titles = listOf(
        "Get Started with Your Aquaponic",
        "Detect Pests",
        "Produce Your Own Organic Plants"
    )
    val descriptions = listOf(
        "Learn how to set up and maintain your aquaponics system for optimal growth.",
        "Use our advanced AI to scan your plants and identify any pests.",
        "Grow fresh, organic produce at home with our guidance and pest management tips."
    )
    val pagerState = rememberPagerState(pageCount = { animations.size })

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier

        ) { page ->
            OnboardingPage(
                animationResId = animations[page],
                title = titles[page],
                description = descriptions[page]
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = animations.size,
            modifier = Modifier
                .padding(top = 65.dp)
                .align(Alignment.CenterHorizontally),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
        )

        ButtonSection(
            pagerState = pagerState,
            navController = navController,
            context = context
        )


    }


}

@Composable
fun ButtonSection(
    pagerState: PagerState,
    navController: NavHostController,
    context: MainActivity
) {
    val coroutineScope = rememberCoroutineScope()

    fun navigateToNextPage() {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = pagerState.pageCount,
                animationSpec = tween(durationMillis = 600)
            )
        }
    }

    fun navigateToPreviousPage() {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = pagerState.currentPage - 1,
                animationSpec = tween(durationMillis = 600)
            )
        }
    }

    Box(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        when (pagerState.currentPage) {
            0 -> {
                Text(
                    text = stringResource(R.string.next_page),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterEnd)
                        .clickable(onClick = ::navigateToNextPage),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            1 -> {
                Text(
                    text = stringResource(R.string.next_page),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterEnd)
                        .clickable(onClick = ::navigateToNextPage),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else -> {
                CustomButton(
                    text = "Get Started",
                    onClick = {
//                        onBoardingFinished(context = context)
                        navController.popBackStack()
                        navController.navigate(Graph.DASHBOARD)
//                        navController.navigate(Graph.LOGIN)
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(
    animationResId: Int,
    title: String,
    description: String
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))

    Column(
        modifier = Modifier.height(450.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .size(250.dp)
        )
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            lineHeight = 38.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = description,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Black,
    inactiveColor: Color = Color.Gray
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        for (i in 0 until pageCount) {
            val color = if (i == pagerState.currentPage) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(8.dp)
                    .background(color, shape = CircleShape)
            )
        }
    }
}

private fun onBoardingFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("Finished", true)
    editor.apply()
}

//@Preview(showBackground = true)
//@Composable
//private fun Temp() {
//    PestifyAppTheme {
//        OnboardingScreen(navController = rememberNavController())
//    }
//}