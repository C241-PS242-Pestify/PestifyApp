package com.learning.pestifyapp

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import com.learning.pestifyapp.ui.components.BottomBarState
import com.learning.pestifyapp.ui.components.BottomNavBar
import com.learning.pestifyapp.ui.screen.navigation.NavigationGraph
import kotlinx.coroutines.launch

@Composable
fun PestifyApp(
    navController: NavHostController = rememberNavController(),
    context: MainActivity,
    modifier: Modifier = Modifier,
) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val scope = rememberCoroutineScope()

    val bottomBarState: BottomBarState = viewModel()
    val isBottomBarVisible = bottomBarState.bottomAppBarState.collectAsState()

    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            "home", "pespedia", "pescan", "bookmark", "profile" -> {
                scope.launch {
                    bottomBarState.setBottomAppBarState(true)
                }
            }

            else -> {
                scope.launch {
                    bottomBarState.setBottomAppBarState(false)
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != null) {
                BottomBar(
                    navController = navController,
                    context = context,
                    bottomBarState = isBottomBarVisible,
                    currentRoute = currentRoute
                )
            }
        },
        modifier = Modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavigationGraph(
                navController = navController,
                context = context,
                bottomBarState = bottomBarState,
            )
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    context: Context,
    bottomBarState: State<Boolean>,
    currentRoute: String,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        visible = isBottomBarVisible(currentRoute = currentRoute, bottomBarState = bottomBarState),
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
fun isBottomBarVisible(currentRoute: String, bottomBarState: State<Boolean>): Boolean {
    val routesWithBottomBar = listOf("home", "pespedia", "pescan", "bookmark", "profile")
    return currentRoute in routesWithBottomBar && bottomBarState.value
}

//@Preview(showBackground = true)
//@Composable
//fun PestifyAppPreview() {
//    PestifyAppTheme() {
//        PestifyApp()
//    }
//}