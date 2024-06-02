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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.launch

@Composable
fun AnimatedStatusBarColorOnScroll(
    context: Context,
    defaultStatusBarColor: Color,
    scrolledStatusBarColor: Color,
    content: @Composable (LazyListState) -> Unit
) {
    val listState = rememberLazyListState()

    // Remember the initial status bar color
    DisposableEffect(Unit) {
        val window = (context as Activity).window
        val originalStatusBarColor = window.statusBarColor

        window.statusBarColor = defaultStatusBarColor.toArgb()

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

    LaunchedEffect(animatedStatusBarColor) {
        val window = (context as Activity).window
        window.statusBarColor = animatedStatusBarColor.toArgb()
    }

    // Pass the listState to the content
    content(listState)
}
