package com.learning.pestifyapp.ui.screen.dashboard.pescan


import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: PescanScreenViewModel,
    navController: NavHostController,
    context: MainActivity,
) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val preview = androidx.camera.core.Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember { ImageCapture.Builder().build() }
    LaunchedEffect(lensFacing) {
        val cameraProvider = withContext(Dispatchers.IO) {
            ProcessCameraProvider.getInstance(context).get()
        }
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Button(onClick = {
            viewModel.captureImage(
                imageCapture,
                context,
                navController,
            )
        }) {
            Icon(
                painterResource(id = R.drawable.ic_opcam),
                contentDescription = null
            )
        }
    }
}
