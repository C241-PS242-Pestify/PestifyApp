package com.learning.pestifyapp.ui.screen.authentication.register


import com.learning.pestifyapp.data.UserRepository
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.UserData
import kotlinx.coroutines.launch

class RegisterScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _user = MutableLiveData<UserData?>()
    val user: LiveData<UserData?> = _user

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isValid = MutableLiveData(false)
    val isValid: LiveData<Boolean> = _isValid

    var emailValue by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set

    var confirmPasswordValue by mutableStateOf("")
        private set
    var confirmPasswordError by mutableStateOf("")
        private set

    fun setEmail(value: String) {
        emailValue = value
    }

    fun setPassword(value: String) {
        passwordValue = value
    }

    fun setConfirmPassword(value: String) {
        confirmPasswordValue = value
    }

    private fun validateEmail(): Boolean {
        val email = emailValue.trim()
        return if (email.isBlank()) {
            emailError = "Email is required"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email address"
            false
        } else {
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = passwordValue.trim()
        return if (password.isBlank()) {
            passwordError = "Please fill password field"
            false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters long"
            false
        } else {
            true
        }
    }

    private fun validateConfirmPassword(): Boolean {
        val confirmPassword = confirmPasswordValue.trim()
        return if (confirmPassword.isBlank()) {
            confirmPasswordError = "Please fill confirm password field"
            false
        } else if (confirmPassword != passwordValue) {
            confirmPasswordError = "Passwords do not match"
            false
        } else {
            true
        }
    }

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateEmail() && validatePassword() && validateConfirmPassword()) {
            _loading.value = true
            viewModelScope.launch {
                val result = userRepository.register(emailValue, passwordValue)
                result.onSuccess { user ->
                    _isValid.value = true
                    _user.value = user
                    onSuccess()
                }
                result.onFailure { error ->
                    _errorMessage.value = error.message
                    onError(error.message ?: "An error occurred")
                }
                _loading.value = false
            }
        }else{
            _isValid.value = false
            onError("Invalid email or password")
        }
    }
}
