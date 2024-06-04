package com.learning.pestifyapp

import android.content.Context
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    Scaffold (
        bottomBar = {
            BottomBar(navController = navController, context = context)
        },
        modifier = Modifier
    ){ innerPadding ->
        NavigationGraph(
            navController = navController,
            context = context,
            innerPadding = innerPadding
        )
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    var shouldShowBottomBar by remember { mutableStateOf(true) }

    LaunchedEffect(currentDestination?.destination?.route) {
        shouldShowBottomBar = when(currentDestination?.destination?.route) {
            null -> false
            "splash" -> false
            "home", "pespedia", "pescan", "history", "profile" -> true
            else -> false
        }
    }

    if (shouldShowBottomBar) {
        BottomNavBar(
            modifier = modifier,
            context = context,
            navController = navController
        )
    }

    //BottomNavBar(navController = navController)
//    daojdasjdj

}

//@Preview(showBackground = true)
//@Composable
//fun PestifyAppPreview() {
//    PestifyAppTheme() {
//        PestifyApp()
//    }
//}