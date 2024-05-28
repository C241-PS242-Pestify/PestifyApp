package com.learning.pestifyapp.ui.screen.authentication.login

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun LoginScreen(
    navController: NavHostController,
    context: MainActivity,
    viewModel: LoginScreenViewModel,
) {
    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("Welcome back!")
        }
        append(" Glad to see you")
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
            Text(
                text = "Forgot Password?",
                modifier = Modifier
                    .clickable { }
                    .padding(top = 11.dp)
                    .align(Alignment.End)
                    .padding(end = 27.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        CustomButton(
            text = "Sign In",
            onClick = {
                focusManager.clearFocus()
                viewModel.login(
                    onSuccess = {
                        navController.navigate(Graph.DASHBOARD)
                    },
                    onError = { errorMessage ->
                        handleLoginError(context, errorMessage)
                    }
                )
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "REGISTER", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate(Graph.REGISTER)
                    }
            }
        )
    }


}
private fun handleLoginError(context: Context, errorMessage: String) {
    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    Handler(Looper.getMainLooper()).postDelayed({
    }, 2000)
}

private fun loginFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("Finished", true)
    editor.apply()
}
