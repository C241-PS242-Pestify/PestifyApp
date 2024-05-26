package com.learning.pestifyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.learning.pestifyapp.ui.nav.BottomNav
import com.learning.pestifyapp.ui.nav.NavGraph
import com.learning.pestifyapp.ui.theme.PestifyAppTheme
import com.learning.pestifyapp.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PestifyAppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        if (authViewModel.isLoggedIn.collectAsState().value) {
                            BottomNav(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
