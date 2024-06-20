package com.learning.pestifyapp.ui.screen.authentication.register

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun RegisterScreen(
    navController: NavHostController,
    context: MainActivity,
    viewModel: RegisterScreenViewModel,
) {
    val isLoading by viewModel.loading.observeAsState(initial = false)

    var isEmailAndPasswordVisible by remember { mutableStateOf(true) }
    var isUsernameVisible by remember { mutableStateOf(false) }

    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFC107))) {
            append("Hello!")
        }
        append(" Sign up to get started")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isEmailAndPasswordVisible) {
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    fontSize = 36.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 50.sp,
                    modifier = Modifier.padding(top = 120.dp, bottom = 30.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
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
                    Spacer(modifier = Modifier.height(15.dp))
                    TextFieldValidation(
                        value = viewModel.confirmPasswordValue,
                        onChange = viewModel::setConfirmPassword,
                        placeholder = "Confirm Password",
                        isError = viewModel.confirmPasswordError.isNotEmpty(),
                        icon = Icons.Rounded.Lock,
                        isPassword = true,
                        errorMessage = viewModel.confirmPasswordError,
                    )
                }
                CustomButton(
                    text = "Sign Up",
                    onClick = {
//                        viewModel.register(
//                            onSuccess = {
//                                Toast.makeText(
//                                    context,
//                                    "Registration Successful",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                isEmailAndPasswordVisible = false
//                                isUsernameVisible = true
//                            },
//                            onError = { errorMessage ->
//                                handleRegistrationError(context, errorMessage)
//                            }
//                        )
                        navController.popBackStack()
                        navController.navigate(Graph.USERNAME)
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    Text(
                        text = "Already have an account?",
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = " Login!",
                        fontWeight = FontWeight.W600,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    navController.popBackStack()
                                    navController.navigate(Graph.LOGIN)
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            if (isUsernameVisible) {
                UsernameScreen(
                    navController = navController,
                    context = context,
                    viewModel = viewModel
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
}

private fun handleRegistrationError(context: Context, errorMessage: String) {
    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    Handler(Looper.getMainLooper()).postDelayed({}, 2000)
}
