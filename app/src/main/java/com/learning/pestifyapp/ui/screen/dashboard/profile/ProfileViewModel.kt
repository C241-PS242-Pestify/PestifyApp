package com.learning.pestifyapp.ui.screen.dashboard.profile

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.Coil
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _userImage = MutableStateFlow<Bitmap?>(null)
    val userImage: StateFlow<Bitmap?> get() = _userImage

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    private val _logoutStatus = MutableStateFlow<String?>(null)
    val logoutStatus: StateFlow<String?> get() = _logoutStatus


    //Text Field Data Value
    private var usernameValue by mutableStateOf("")
    var usernameError by mutableStateOf("")
        private set

    private var emailValue by mutableStateOf("")
    var emailError by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
    var passwordError by mutableStateOf("")
        private set

    var confirmPasswordValue by mutableStateOf("")
        private set
    var confirmPasswordError by mutableStateOf("")
        private set



    private fun validateUsername(): Boolean {
        val username = usernameValue.trim()
        if (username.isNotEmpty() && username.length < 6) {
            usernameError = "Username must be at least 5 characters long"
            return false
        } else if (username.contains(" ")) {
            usernameError = "Username should not contain spaces"
            return false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        val email = emailValue.trim()
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email address"
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        val password = passwordValue.trim()
        val containsLetter = password.any { it.isLetter() }
        val containsDigit = password.any { it.isDigit() }
        return when {
            password.length < 6 -> {
                passwordError = "Password must be at least 6 characters long"
                false
            }

            !containsLetter -> {
                passwordError = "Password must contain at least one letter"
                false
            }

            !containsDigit -> {
                passwordError = "Password must contain at least one digit"
                false
            }

            else -> true
        }
    }
    fun updateAccount(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        usernameValue = name
        emailValue = email
        passwordValue = password

        if (!validateUsername() && !validateEmail() && !validatePassword()) {
            return onError("Please fill in the required fields")
        }
        viewModelScope.launch {
            Log.d(
                "TAG",
                "updateAccount called with name: $name, email: $email, password: $password"
            )
            when (val result = userRepository.updateAccount(name, email, password)) {
                is ResultResponse.Success -> {
                    _userData.value = result.data
                    usernameValue = result.data.username ?: ""
                    emailValue = result.data.email ?: ""
                    Log.d("TAG", "updateAccount: ${result.data}")
                    onSuccess()
                }

                is ResultResponse.Error -> {
                    Log.e("TAG", "updateAccount error: ${result.error}")
                    onError(result.error)
                }

                ResultResponse.Loading -> {
                    Log.d("TAG", "updateAccount loading")
                }
            }
        }
    }



    fun fetchUserData() {
        viewModelScope.launch {
            val userData = userRepository.getUserSession()
            _userData.value = userData
        }
    }
    fun uploadProfilePhoto(photoUrl: String, context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("ProfileScreen", "Uploading photo URL: $photoUrl")
                val response = userRepository.uploadProfilePhoto(photoUrl)
                if (response.isSuccessful) {
                    Log.d("ProfileScreen", "Upload successful: ${response.body()?.message}")
                    saveProfilePhotoUri(photoUrl)
                    updateProfilePhoto(photoUrl)
                    clearImageCache(context)
                    userRepository.fetchUserData()
                    onSuccess()
                } else {
                    Log.e("ProfileScreen", "Upload failed: ${response.message()}")
                    onError("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Exception: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun saveProfilePhotoUri(uri: String) {
        viewModelScope.launch {
            userRepository.saveProfilePhotoUri(uri)
        }
    }

    fun updateProfilePhoto(photoUrl: String) {
        viewModelScope.launch {
            val updatedUserData = _userData.value?.copy(profilePhoto = photoUrl)
            _userData.value = updatedUserData
        }
    }
    fun clearImageCache(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Coil.imageLoader(context).diskCache?.clear()
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            when (val result = userRepository.logout()) {
                is ResultResponse.Success -> {
                    _logoutStatus.value = result.data
                }

                is ResultResponse.Error -> {
                    _logoutStatus.value = result.error
                }

                ResultResponse.Loading -> {
                    Log.d("TAG", "logout loading")
                }
            }
        }
    }
}