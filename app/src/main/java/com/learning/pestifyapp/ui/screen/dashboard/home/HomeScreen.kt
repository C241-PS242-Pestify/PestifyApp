package com.learning.pestifyapp.ui.screen.dashboard.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.screen.navigation.Graph

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = viewModel.fetchUsername() ?: "", modifier = Modifier.padding(start = 16.dp, top = 16.dp))

            Text(stringResource(R.string.menu_home))

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(Graph.LOGIN) {
                        popUpTo(Graph.LOGIN) { inclusive = true }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Logout")
            }
        }
    }
}
