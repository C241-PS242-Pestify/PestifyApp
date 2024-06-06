package com.learning.pestifyapp.ui.components

import android.app.Activity
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun AnimatedStatusBarColorOnScroll(
    context: Context,
    defaultStatusBarColor: Color,
    scrolledStatusBarColor: Color,
    isDefaultStatusBarIconsDark: Boolean,
    isScrolledStatusBarIconsDark: Boolean,
    content: @Composable (LazyListState) -> Unit
) {
    val listState = rememberLazyListState()

    DisposableEffect(Unit) {
        val window = (context as Activity).window
        val originalStatusBarColor = window.statusBarColor
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        window.statusBarColor = defaultStatusBarColor.toArgb()
        windowInsetsController.isAppearanceLightStatusBars = isDefaultStatusBarIconsDark

        onDispose {
            window.statusBarColor = originalStatusBarColor
        }
    }

    val scrollOffset by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset
        }
    }

    val animatedStatusBarColor by animateColorAsState(
        targetValue = if (scrollOffset > 0) scrolledStatusBarColor else defaultStatusBarColor,
        label = ""
    )

    LaunchedEffect(animatedStatusBarColor, scrollOffset) {
        val window = (context as Activity).window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        window.statusBarColor = animatedStatusBarColor.toArgb()
        windowInsetsController?.isAppearanceLightStatusBars = if (scrollOffset > 0) isScrolledStatusBarIconsDark else isDefaultStatusBarIconsDark
    }

    content(listState)
}
