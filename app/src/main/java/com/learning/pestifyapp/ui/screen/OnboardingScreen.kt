package com.learning.pestifyapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.navigation.NavHostController
import com.learning.pestifyapp.ui.nav.Screen

// OnBoardingScreen.kt
@Composable
fun OnBoardingScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Welcome to Onboarding Screen",
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { navController.navigate(Screen.Login.route) }
        ) {
            Text("Login")
        }
        Button(
            onClick = { navController.navigate(Screen.Register.route) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Register")
        }
    }
}

