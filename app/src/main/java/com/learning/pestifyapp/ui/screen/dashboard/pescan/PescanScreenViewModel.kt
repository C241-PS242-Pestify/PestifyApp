package com.learning.pestifyapp.ui.screen.dashboard.pescan

import ImagePagingSource
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.learning.pestifyapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PescanScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> get() = _imageUri
    
    fun updateImageUri(uri: Uri) {
        _imageUri.value = uri
    }
    fun captureImage(
        imageCapture: ImageCapture,
        context: Context,
        navController: NavHostController,
    ) {
        val name = "CameraxImage.jpeg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri
                    if (savedUri != null) {
                        updateImageUri(savedUri)
                        navController.popBackStack()
                    } else {
                        // Handle error
                    }
                }
                override fun onError(exception: ImageCaptureException) {
                    // Handle error
                }
            }
        )
    }

    fun loadImagesFromGallery(context: Context): List<Uri> {
        val uris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                uris.add(contentUri)
            }
        }
        return uris
    }
}