package com.learning.pestifyapp.ui.screen.authentication.register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.components.CustomButton
import com.learning.pestifyapp.ui.components.TextFieldValidation
import com.learning.pestifyapp.ui.screen.navigation.Graph
import com.learning.pestifyapp.ui.theme.base80

@Composable
fun UsernameScreen(
    navController: NavHostController,
    context: MainActivity,
    viewModel: RegisterScreenViewModel,
) {
    val focusManager = LocalFocusManager.current
    val isLoading by viewModel.loading.collectAsState(initial = false)
    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFC107))) {
            append("Account Created!")
        }
        append(" How should we call you?")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 50.sp,
                modifier = Modifier.padding(top = 120.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = context.getString(R.string.instruction),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = base80,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 50.sp,
                    modifier = Modifier.padding(bottom = 15.dp)
                )
                TextFieldValidation(
                    value = viewModel.usernameValue,
                    onChange = viewModel::setUsername,
                    placeholder = "Username",
                    isError = viewModel.usernameError.isNotEmpty(),
                    icon = Icons.Rounded.Person,
                    errorMessage = viewModel.usernameError,
                )
            }
            CustomButton(
                text = "Done",
                onClick = {
                    focusManager.clearFocus()
                    viewModel.saveUsername(
                        onSuccess = {
                            navController.popBackStack()
                            navController.navigate(Graph.LOGIN)
                            Toast.makeText(context, "Account Created!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { errorMessage ->
                            handleLoginError(context, errorMessage)
                        }
                    )
                },
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

private fun handleLoginError(context: Context, errorMessage: String) {
    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
}
