package com.learning.pestifyapp.ui.screen.dashboard.profile

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.components.CustomButton
import com.learning.pestifyapp.ui.components.TextFieldValidation
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PestViewModel

@Composable
fun ChangePasswordScreen(
    navController: NavHostController,
    context: MainActivity,
    viewModel: ProfileViewModel,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 14.dp)
                    .align(Alignment.Start)
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(0.5.dp, Color.Gray, CircleShape)
                        .size(34.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "Navigate Back"
                    )
                }
                Box(
                    modifier = Modifier.weight(1f).padding(end = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.changepas),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
            }
            FormSection(context, navController, viewModel)
        }
    }
}

@Composable
fun FormSection(
    context: MainActivity,
    navController: NavHostController,
    viewModel: ProfileViewModel,
) {
    val userData by viewModel.userData.collectAsState()
    val username by remember { mutableStateOf(userData?.username ?: "") }
    val email by remember { mutableStateOf(userData?.email ?: "") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "New Password",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(
                    Alignment.Start
                )
            )
            TextFieldValidation(
                value = password,
                onChange = { password = it },
                placeholder = "",
                isError = viewModel.passwordError.isNotEmpty(),
                icon = Icons.Default.Lock,
                errorMessage = viewModel.passwordError,
                isPassword = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Confirm New Password",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(
                    Alignment.Start
                )
            )
            TextFieldValidation(
                value = confirmPassword,
                onChange = { confirmPassword = it },
                placeholder = "",
                isError = viewModel.confirmPasswordError.isNotEmpty(),
                icon = Icons.Default.Lock,
                errorMessage = viewModel.confirmPasswordError,
                isPassword = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomButton(
                text = "Change Password",
                onClick = {
                    focusManager.clearFocus()
                    if (password != confirmPassword) {
                        Toast.makeText(
                            context,
                            "Password and Confirm Password must be the same",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.updateAccount(
                            name = username,
                            email = email,
                            password = password,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Password Change Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("profile")
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

    }
}