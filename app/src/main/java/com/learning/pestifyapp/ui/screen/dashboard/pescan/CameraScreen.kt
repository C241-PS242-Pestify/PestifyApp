package com.learning.pestifyapp.ui.screen.dashboard.pescan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.components.CaptureButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: PestViewModel,
    navController: NavHostController,
    context: MainActivity,
) {
    val lensFacing by viewModel.lensFacing.collectAsState(CameraSelector.LENS_FACING_BACK)
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val preview = androidx.camera.core.Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember { ImageCapture.Builder().build() }
    val isLoading by viewModel.isLoading.collectAsState()

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    viewModel.updateImageUri(it)
                    viewModel.predictPest(context, onSuccess = { response ->
                        viewModel.updatePestResponse(response)
                        navController.navigate("result_screen/${Uri.encode(it.toString())}")
                    }, onError = {
                        // TODO: Handle error
                    })
                }
            }
        }

    LaunchedEffect(lensFacing) {
        val cameraProvider = withContext(Dispatchers.IO) {
            ProcessCameraProvider.getInstance(context).get()
        }
        withContext(Dispatchers.Main) {
            cameraProvider.unbindAll()
        }
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) { detectTapGestures {} }
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 14.dp, vertical = 14.dp)
                .clickable { navController.popBackStack() }) {
            Icon(
                painter = painterResource(id = R.drawable.cross),
                contentDescription = "Open Gallery",
                modifier = Modifier
                    .size(34.dp),
                tint = Color.White
            )
        }

        BottomSection(
            imageCapture = imageCapture,
            viewModel = viewModel,
            context = context,
            navController = navController,
            openGallery = {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryLauncher.launch(intent)
            },
            isLoading = isLoading,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun BottomSection(
    modifier: Modifier = Modifier,
    imageCapture: ImageCapture,
    viewModel: PestViewModel,
    context: MainActivity,
    navController: NavHostController,
    openGallery: () -> Unit,
    isLoading: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                    .then(if (!isLoading) Modifier.clickable { openGallery() } else Modifier),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gallery_24),
                    contentDescription = "Open Gallery",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CaptureButton {
                    if (!isLoading) {
                        viewModel.captureImage(
                            imageCapture,
                            context,
                            navController,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
                    .then(if (!isLoading) Modifier.clickable { viewModel.rotateCamera() } else Modifier),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_rotate_left_24),
                    contentDescription = "Rotate Camera",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}