package com.learning.pestifyapp.ui.screen.dashboard.pescan

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.learning.pestifyapp.data.model.pestdata.PestData
import com.learning.pestifyapp.data.model.remote.PestResponse
import com.learning.pestifyapp.data.model.remote.SavePredictionResponse
import com.learning.pestifyapp.data.repository.HistoryRepository
import com.learning.pestifyapp.data.repository.PestRepository
import com.learning.pestifyapp.ui.common.UiState
import com.learning.pestifyapp.ui.common.compressBitmap
import com.learning.pestifyapp.ui.common.converterBitmapToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream

class PestViewModel(
    private val pestRepository: PestRepository,
    private val historyRepository: HistoryRepository,
) : ViewModel() {
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> get() = _imageUri
    private val _pestResponse = MutableStateFlow<PestResponse?>(null)
    val pestResponse: StateFlow<PestResponse?> get() = _pestResponse
    private val _isLoading = MutableStateFlow(false)

    private val _lensFacing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    val lensFacing: StateFlow<Int> = _lensFacing
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _savePredictionState =
        MutableStateFlow<UiState<SavePredictionResponse>>(UiState.Empty)
    val savePredictionState: StateFlow<UiState<SavePredictionResponse>> get() = _savePredictionState

    init {
        loadPestDataFromPreferences()
    }

    fun rotateCamera() {
        _lensFacing.value = if (_lensFacing.value == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
    }

    fun updateImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun updatePestResponse(response: PestResponse) {
        _pestResponse.value = response
        savePestDataToPreferences(response)
    }

    private fun savePestDataToPreferences(response: PestResponse) {
        val pestData = response.data?.let {
            PestData(
                id = it.id,
                pestName = it.pestName,
                pestDescription = it.pestDescription,
                pestCause = it.pestCause,
                pestEffect = it.pestEffect,
                solution = it.solution,
                confidenceScore = it.confidenceScore
            )
        }
        if (pestData != null) {
            pestRepository.savePestData(pestData)
        }
    }

    fun loadPestDataFromPreferences() {
        val pestData = pestRepository.getPestData()
        if (pestData != null) {
            _pestResponse.value = PestResponse(
                status = "success",
                message = "Loaded from preferences",
                data = pestData
            )
        }
    }

    fun predictPest(
        context: Context,
        onSuccess: (PestResponse) -> Unit,
        onError: (String) -> Unit,
    ) {
        val imageUri = _imageUri.value ?: return
        val contentResolver = context.contentResolver
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            inputStream?.use {
                val tempFile = File.createTempFile("temp_image", ".jpeg", context.cacheDir)
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", tempFile.name, requestFile)

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val response = pestRepository.predictPest(body)
                        if (response.isSuccessful) {
                            response.body()?.let {
                                val pestData = PestData(
                                    id = it.data?.id ?: "",
                                    pestName = it.data?.pestName,
                                    pestDescription = it.data?.pestDescription,
                                    pestCause = it.data?.pestCause,
                                    pestEffect = it.data?.pestEffect,
                                    solution = it.data?.solution,
                                    confidenceScore = it.data?.confidenceScore ?: 0.0,
                                    additionalImage = it.data?.additionalImage
                                )
                                pestRepository.savePestData(pestData)
                                withContext(Dispatchers.Main) {
                                    onSuccess(it)
                                }
                            } ?: run {
                                withContext(Dispatchers.Main) {
                                    onError("Response body is null")
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                onError("Prediction failed: ${response.message()}")
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            onError("Prediction failed: ${e.message}")
                        }
                    }
                }
            } ?: run {
                onError("Failed to open input stream from URI")
            }
        } catch (e: Exception) {
            onError("Failed to read image file: ${e.message}")
        }
    }

    fun captureImage(
        imageCapture: ImageCapture,
        context: Context,
        navController: NavHostController,
    ) {
        _isLoading.value = true
        val name = "CameraxImage_${System.currentTimeMillis()}.jpeg"
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
                        predictPest(context, onSuccess = { response ->
                            _isLoading.value = false
                            updatePestResponse(response)
                            viewModelScope.launch {
                                withContext(Dispatchers.Main) {
                                    navController.navigate("result_screen/${Uri.encode(savedUri.toString())}")
                                }
                            }
                        }, onError = { error ->

                        })
                    } else {
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    _isLoading.value = false
                }
            }
        )
    }

    fun savePrediction(predictionId: String, bitmap: Bitmap) {
        viewModelScope.launch {
            _savePredictionState.value = UiState.Loading
            try {
                val response = pestRepository.savePrediction(predictionId)
                _savePredictionState.value = UiState.Success(response)
                val compressedBitmap = compressBitmap(bitmap, 20)
                val encodedImage = converterBitmapToString(compressedBitmap)
                historyRepository.saveHistoryImage(predictionId, encodedImage)
                Log.d(
                    "PescanScreenViewModel",
                    "Image saved to local database with ID: $predictionId"
                )
            } catch (e: Exception) {
                _savePredictionState.value = UiState.Error(e.message ?: "An unknown error occurred")
                Log.e("PescanScreenViewModel", "Failed to save image: ${e.message}")
            }
        }
    }


    fun DeletePrediction(predictionId: String) {
        viewModelScope.launch {
            _savePredictionState.value = UiState.Loading
            try {
                val response = pestRepository.delPrediction(predictionId)
                _savePredictionState.value = UiState.Success(response)
            } catch (e: Exception) {
                _savePredictionState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}