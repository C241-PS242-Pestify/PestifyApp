package com.learning.pestifyapp

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import com.learning.pestifyapp.ui.components.BottomNavBar
import com.learning.pestifyapp.ui.screen.navigation.NavigationGraph
import com.learning.pestifyapp.ui.screen.navigation.getNavigationItems
import com.learning.pestifyapp.ui.screen.navigation.getNavigationRoute
import com.learning.pestifyapp.ui.theme.PestifyAppTheme

@Composable
fun PestifyApp(
    navController: NavHostController = rememberNavController(),
    context: MainActivity,
    modifier: Modifier = Modifier,
) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


    val bottomAppBarState = rememberSaveable {
        mutableStateOf(true)
    }

    when (currentRoute) {
        "home", "pespedia", "pescan", "bookmark", "profile" -> {
            bottomAppBarState.value = true
        }
        else -> {
            bottomAppBarState.value = false
        }
    }

    Scaffold (
        bottomBar = {
            BottomBar(navController = navController, context = context, bottomAppBarState = bottomAppBarState)
        },
        modifier = Modifier
    ){ innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavigationGraph(
                navController = navController,
                context = context,
            )
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    context: Context,
    bottomAppBarState: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        visible = bottomAppBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavBar(
            context = context,
            navController = navController,
            modifier = modifier
        )
    }

}

//@Preview(showBackground = true)
//@Composable
//fun PestifyAppPreview() {
//    PestifyAppTheme() {
//        PestifyApp()
//    }
//}