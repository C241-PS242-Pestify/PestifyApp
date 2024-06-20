package com.learning.pestifyapp.ui.screen.dashboard.pescan

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.ui.screen.navigation.Graph

@Composable
fun PestScreen(
    navController: NavHostController,
) {
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            navController.popBackStack()
            navController.navigate(Graph.CAMERA)
        } else {
            // Handle permission denied
            Log.d("PescanScreen", "Camera permission denied")
        }
    }
    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}