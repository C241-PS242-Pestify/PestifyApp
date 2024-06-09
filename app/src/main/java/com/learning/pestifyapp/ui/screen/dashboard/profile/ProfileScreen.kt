package com.learning.pestifyapp.ui.screen.dashboard.profile

import ImageAlertDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.ui.components.CustomAlertDialog
import com.learning.pestifyapp.ui.screen.navigation.Graph


@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel,
    navController: NavHostController,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    val userData by viewModel.userData.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.observeAsState(initial = true)
    val isDataLoaded by viewModel.isDataLoaded.observeAsState()
    var showChangeProfilePictureDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri
            showChangeProfilePictureDialog = true
        }

    if (showChangeProfilePictureDialog) {
        ImageAlertDialog(
            title = "Change Profile Picture",
            message = "Are you sure you want to change your profile picture?",
            imageUri = selectedImageUri ?: Uri.EMPTY,
            onYesClick = {
                showChangeProfilePictureDialog = false
            },
            onNoClick = {
                showChangeProfilePictureDialog = false
            },
            onDismiss = {
                showChangeProfilePictureDialog = false
            }
        )
    }
    if (showDialog) {
        CustomAlertDialog(
            title = "Confirm",
            message = "Are you sure you want to log out?",
            onYesClick = {
                showDialog = false
                navController.popBackStack()
                viewModel.logout()
                Toast
                    .makeText(context, "Logout", Toast.LENGTH_SHORT)
                    .show()
                navController.navigate("login")
            },
            onNoClick = { showDialog = false },
            onDismiss = { showDialog = false }
        )
    }

    if (isLoading || !isDataLoaded!!) {
        Box(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopSection(
                userData = userData,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            )
            AccountSection(
                navController = navController,
                showDialog = showDialog,
                setShowDialog = { showDialog = it },
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel,
                launcher = launcher,
                selectedImageUri = selectedImageUri, // tambahkan ini
                showChangeProfilePictureDialog = showChangeProfilePictureDialog,
                setShowChangeProfilePictureDialog = { showChangeProfilePictureDialog = it }
            )
        }
    }
}

@Composable
fun TopSection(
    userData: UserData?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = userData?.username ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    alignment = Alignment.TopEnd,
                    painter = painterResource(id = R.drawable.sherlock_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }
    }
}

@Composable
fun AccountSection(
    navController: NavHostController,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel,
    launcher: ActivityResultLauncher<String>,
    selectedImageUri: Uri?,
    showChangeProfilePictureDialog: Boolean,
    setShowChangeProfilePictureDialog: (Boolean) -> Unit,
) {

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column {
                Text(
                    text = "My Account",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier
                        .padding(bottom = 4.dp, top = 16.dp)
                        .fillMaxWidth()
                        .clickable {
                            launcher.launch("image/*")
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Change Profile Picture!",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Arrow Icon",
                        tint = Color.Black
                    )
                }


                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                Row(
                    modifier = Modifier
                        .padding(bottom = 4.dp, top = 16.dp)
                        .clickable {
                            navController.navigate(Graph.PRIVACY)
                        }
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Privacy",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = Color.Black
                    )
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Arrow Icon",
                        tint = Color.Black
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(19.dp))
            Text(
                text = "General",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp, top = 16.dp)
                    .fillMaxWidth()
                    .clickable {

                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Terms and Conditions",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow Icon",
                    tint = Color.Black
                )
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp, top = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        setShowDialog(true)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Log Out",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = Color.Black
                )
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow Icon",
                    tint = Color.Black
                )
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        }
    }
}

