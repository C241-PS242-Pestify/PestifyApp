package com.learning.pestifyapp.ui.screen.authentication.register

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

@Composable
fun RegisterScreen(
    navController: NavHostController,
    context: MainActivity,
    viewModel: RegisterScreenViewModel,
) {
    val isLoading by viewModel.loading.observeAsState(initial = false)
    val focusManager = LocalFocusManager.current
    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFC107))) {
            append("Hello!")
        }
        append(" Sign up to get started")
    }
    val text = "Already have an account? Login"
    val annotatedString = buildAnnotatedString {
        append(text)
        addStringAnnotation(
            tag = "LOGIN",
            annotation = "Login",
            start = text.indexOf("Login"),
            end = text.length
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp,
                modifier = Modifier.padding(start = 24.dp)
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
                    modifier = Modifier
                        .padding(horizontal = 22.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextFieldValidation(
                    value = viewModel.passwordValue,
                    onChange = viewModel::setPassword,
                    placeholder = "Password",
                    isError = viewModel.passwordError.isNotEmpty(),
                    icon = Icons.Rounded.Lock,
                    isPassword = true,
                    errorMessage = viewModel.passwordError,
                    modifier = Modifier
                        .padding(horizontal = 22.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextFieldValidation(
                    value = viewModel.confirmPasswordValue,
                    onChange = viewModel::setConfirmPassword,
                    placeholder = "Confirm Password",
                    isError = viewModel.confirmPasswordError.isNotEmpty(),
                    icon = Icons.Rounded.Lock,
                    isPassword = true,
                    errorMessage = viewModel.confirmPasswordError,
                    modifier = Modifier
                        .padding(horizontal = 22.dp)
                )
            }
            CustomButton(
                text = "Sign Up",
                onClick = {
                    focusManager.clearFocus()
                    viewModel.register(
                        onSuccess = {
                            registerFinished(context)
                            navController.navigate(Graph.USERNAME)
                        },
                        onError = { errorMessage ->
                            handleRegistrationError(context, errorMessage)
                        }
                    )
                },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Already have an account?",
                )
                Text(
                    text = " Login!",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Graph.LOGIN)
                        },
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

private fun handleRegistrationError(context: Context, errorMessage: String) {
    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    Handler(Looper.getMainLooper()).postDelayed({
    }, 2000)
}

private fun registerFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("register", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("Finished", true)
    editor.apply()
}