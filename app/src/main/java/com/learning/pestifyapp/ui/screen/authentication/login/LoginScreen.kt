package com.learning.pestifyapp.ui.screen.authentication.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.ui.components.CustomButton
import com.learning.pestifyapp.ui.components.TextFieldValidation
import com.learning.pestifyapp.ui.screen.navigation.Graph
import com.learning.pestifyapp.ui.screen.navigation.Screen


@Composable
fun LoginScreen(
    navController: NavHostController,
    context: MainActivity,
    viewModel: LoginScreenViewModel,
) {
    val isLoading by viewModel.loading.collectAsState(initial = false)
    val focusManager = LocalFocusManager.current
    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFC107))) {
            append("Welcome back!")
        }
        append("\nGlad to see you")

    }
    val text = "Don't Have Account? Register"
    val annotatedString = buildAnnotatedString {
        append(text)
        addStringAnnotation(
            tag = "REGISTER",
            annotation = "Register",
            start = text.indexOf("Register"),
            end = text.length
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 50.sp,
                modifier = Modifier.padding(top = 120.dp, bottom = 30.dp).align(Alignment.Start)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextFieldValidation(
                    value = viewModel.emailValue,
                    onChange = viewModel::setEmail,
                    placeholder = "Email",
                    isError = viewModel.emailError.isNotEmpty(),
                    icon = Icons.Rounded.Email,
                    errorMessage = viewModel.emailError,
                    keyboardType = KeyboardType.Email,
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextFieldValidation(
                    value = viewModel.passwordValue,
                    onChange = viewModel::setPassword,
                    placeholder = "Password",
                    isError = viewModel.passwordError.isNotEmpty(),
                    icon = Icons.Rounded.Lock,
                    isPassword = true,
                    errorMessage = viewModel.passwordError,
                )
                Text(
                    text = "Forgot Password?",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Graph.FORGOT_PASSWORD)
                        }
                        .align(Alignment.End)
                        .padding(bottom = 55.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            CustomButton(
                text = "Sign In",
                onClick = {
                    focusManager.clearFocus()
                    viewModel.login(
                        onSuccess = {
                            navController.popBackStack()
                            navController.navigate(Screen.Home.route)
                        },
                        onError = { errorMessage ->
                            handleLoginError(context, errorMessage)
                        }
                    )
                },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 15.dp)
            ) {
                Text(
                    text = "Don't Have Account?",
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = " Register!",
                    fontWeight = FontWeight.W600,
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                navController.popBackStack()
                                navController.navigate(Graph.REGISTER)
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
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