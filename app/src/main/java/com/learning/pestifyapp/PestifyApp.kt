package com.learning.pestifyapp

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.learning.pestifyapp.ui.components.BottomNavBar
import com.learning.pestifyapp.ui.screen.navigation.NavigationGraph
import com.learning.pestifyapp.ui.screen.navigation.navigationItems
import com.learning.pestifyapp.ui.theme.PestifyAppTheme

@Composable
fun PestifyApp(
    navController: NavHostController = rememberNavController(),
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    Scaffold (
        bottomBar = {
            BottomBar(navController = navController)
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
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavigation = navigationItems.any { it.screen.route == currentRoute }
    if (bottomNavigation) {
        BottomNavBar(
            modifier = modifier,
            navController = navController
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