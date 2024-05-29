package com.learning.pestifyapp.ui.screen.authentication.username

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.ui.components.CustomButton
import com.learning.pestifyapp.ui.components.TextFieldValidation
import com.learning.pestifyapp.ui.screen.navigation.Graph

@Composable
fun UsernameScreen(
    navController: NavHostController,
    context: MainActivity,
    viewModel: UsernameScreenViewModel,
) {
    val title = "How should we call you?"
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            lineHeight = 38.sp,
            modifier = Modifier.padding(8.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldValidation(
                value = viewModel.usernameValue,
                onChange = viewModel::setUsername,
                placeholder = "Username",
                isError = viewModel.usernameError.isNotEmpty(),
                icon = Icons.Rounded.Person,
                errorMessage = viewModel.usernameError,
                modifier = Modifier
                    .padding(horizontal = 22.dp)
            )
        }
        CustomButton(
            text = "Done",
            onClick = {
                focusManager.clearFocus()
                if (viewModel.validateUsername()) {
                    viewModel.saveUsername()
                    navController.navigate(Graph.LOGIN)
                }
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
