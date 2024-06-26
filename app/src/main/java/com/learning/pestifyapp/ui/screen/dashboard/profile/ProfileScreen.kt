package com.learning.pestifyapp.ui.screen.dashboard.profile

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.ui.components.CustomAlertDialog
import com.learning.pestifyapp.ui.screen.navigation.Graph

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    val userData by viewModel.userData.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchUserData()
    }

    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        CustomAlertDialog(
            title = "Confirm",
            message = "Are you sure you want to log out?",
            onYesClick = {
                showDialog = false
                navController.popBackStack()
                viewModel.logout()
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                navController.navigate("login")
            },
            onNoClick = { showDialog = false },
            onDismiss = { showDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)
        .padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.menu_profile),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Top(
                userData = userData,
                navController = navController,
                context = context
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(id = R.string.general),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(20.dp))
            General(
                setShowDialog = { showDialog = it },
                navController = navController
            )
        }
    }
}
@Composable
fun Top(
    userData: UserData?,
    navController: NavHostController,
    context: Context
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Gray.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 19.dp, vertical = 21.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = userData?.username ?: "",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = userData?.email ?: "",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.edit_24dp_fill1_wght300_grad0_opsz24),
                contentDescription = "Arrow Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigate(Graph.EDIT_PROFILE)
                    }
            )
        }
    }
}

@Composable
fun General(
    navController: NavHostController,
    setShowDialog: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Gray.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 23.dp, vertical = 25.dp)

        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable {
                        navController.navigate(Graph.CHANGE_PASSWORD)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.password_lock),
                    contentDescription = "Arrow Icon",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(13.dp))
                Text(
                    modifier = Modifier,
                    text = "Change Password",
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(121.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow Icon",
                    tint = Color.Gray
                )
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        setShowDialog(true)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logout_24dp_fill1_wght300_grad0_opsz24),
                    contentDescription = "Arrow Icon",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(13.dp))
                Text(
                    modifier = Modifier,
                    text = "Logout",
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(195.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow Icon",
                    tint = Color.Gray
                )
            }
        }
    }
}