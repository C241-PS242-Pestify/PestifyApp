package com.learning.pestifyapp.ui.screen.dashboard.profile

import ImageAlertDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.components.CustomButton
import com.learning.pestifyapp.ui.components.TextFieldValidation
import com.learning.pestifyapp.ui.screen.navigation.Screen

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier,
) {
    val userData by viewModel.userData.collectAsState()
    var username by remember { mutableStateOf(userData?.username ?: "") }
    var email by remember { mutableStateOf(userData?.email ?: "") }
    val password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var showChangeProfilePictureDialog by remember { mutableStateOf(false) }
    val selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchUserData()
    }

    if (showChangeProfilePictureDialog) {
        ImageAlertDialog(
            imageUri = selectedImageUri ?: Uri.EMPTY,
            onYesClick = { uriString ->
                viewModel.uploadProfilePhoto(
                    photoUrl = uriString,
                    context = context, // Pass context here
                    onSuccess = {
                        Log.d("ProfileScreen", "Profile picture updated successfully")
                        Toast.makeText(context, "Profile picture updated", Toast.LENGTH_SHORT).show()
                        viewModel.updateProfilePhoto(uriString) // Memperbarui foto profil di ViewModel
                        viewModel.saveProfilePhotoUri(uriString)
                        navController.navigate(Screen.Profile.route) {
                            popUpTo("edit_profile") { inclusive = true }
                            launchSingleTop = true
                            showChangeProfilePictureDialog = false
                        }
                    },
                    onError = { errorMessage ->
                        Log.e("ProfileScreen", "Failed to update profile picture: $errorMessage")
                        Toast.makeText(context, "Failed to update profile picture", Toast.LENGTH_SHORT).show()
                        showChangeProfilePictureDialog = false
                    }
                )
            },
            onNoClick = {
                showChangeProfilePictureDialog = false
            },
            onDismiss = {
                showChangeProfilePictureDialog = false
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.edtprofile),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
                Spacer(modifier = Modifier.width(48.dp))
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(userData?.profilePhoto)
                            .apply {
                                crossfade(true)
                                diskCachePolicy(CachePolicy.ENABLED) // Enable disk caching to reduce load times
                                memoryCachePolicy(CachePolicy.ENABLED) // Enable memory caching to reduce load times
                                fallback(R.drawable.sherlock_profile)
                                placeholder(R.drawable.placeholder)
                            }.build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
                IconButton(
                    onClick = {
                        showChangeProfilePictureDialog = true
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit_24dp_fill1_wght300_grad0_opsz24),
                        contentDescription = "Edit Icon",
                        tint = Color.White,
                    )
                }
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
                    isError = viewModel.usernameError.isNotEmpty(),
                    icon = Icons.Default.Person,
                    errorMessage = viewModel.usernameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Text(text = "Email", style = MaterialTheme.typography.bodyMedium)
                TextFieldValidation(
                    value = email,
                    onChange = { email = it },
                    placeholder = userData?.email ?: "Email",
                    isError = viewModel.emailError.isNotEmpty(),
                    icon = Icons.Default.Email,
                    errorMessage = viewModel.emailError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                CustomButton(
                    text = "Update Account",
                    onClick = {
                        Log.d("EditProfileScreen", "Update Account Clicked with username: $username, email: $email")
                        viewModel.updateAccount(
                            name = username,
                            email = email,
                            password = password,
                            onSuccess = {
                                Log.d("EditProfileScreen", "Account Update Successful")
                                Toast.makeText(
                                    context,
                                    "Account Update Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("profile")
                            },
                            onError = { errorMessage ->
                                Log.e("EditProfileScreen", "Account Update Failed: $errorMessage")
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
