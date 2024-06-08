package com.learning.pestifyapp.ui.screen.dashboard.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.ui.components.CustomButton
import com.learning.pestifyapp.ui.components.TextFieldValidation

@Composable
fun PrivacyScreen(
    viewModel: ProfileScreenViewModel,
    navController: NavHostController,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    val userData by viewModel.userData.observeAsState()

    var username by remember { mutableStateOf(userData?.username ?: "") }
    var email by remember { mutableStateOf(userData?.email ?: "") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Privacy",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(48.dp)) // Tambahkan ruang kosong di sebelah kanan
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Username", style = MaterialTheme.typography.bodyMedium)
                TextFieldValidation(
                    value = username,
                    onChange = { username = it },
                    placeholder = userData?.username ?: "Username",
                    isError = false,
                    icon = Icons.Default.Person,
                    errorMessage = "",
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Text(text = "Email", style = MaterialTheme.typography.bodyMedium)
                TextFieldValidation(
                    value = email,
                    onChange = { email = it },
                    placeholder = userData?.email ?: "Email",
                    isError = false,
                    icon = Icons.Default.Email,
                    errorMessage = "",
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Text(text = "Password", style = MaterialTheme.typography.bodyMedium)
                TextFieldValidation(
                    value = password,
                    onChange = { password = it },
                    placeholder = "Enter new password",
                    isError = false,
                    icon = Icons.Default.Lock,
                    errorMessage = "",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }

            CustomButton(
                text = "Update Account",
                onClick = {
                    focusManager.clearFocus()
                    viewModel.updateAccount(
                        name = username,
                        email = email,
                        password = password,
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Account updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("profile")
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
