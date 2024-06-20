package com.learning.pestifyapp.ui.screen.authentication.register


import android.util.Log
import com.learning.pestifyapp.data.repository.UserRepository
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var email = ""
    private var password = ""

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isValid = MutableStateFlow(false)
    val isValid: StateFlow<Boolean> = _isValid

    var usernameValue by mutableStateOf("")
        private set
    var usernameError by mutableStateOf("")
        private set

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


    fun setUsername(value: String) {
        usernameValue = value
    }

    fun setEmail(value: String) {
        emailValue = value
    }

    fun setPassword(value: String) {
        passwordValue = value
    }

    fun setConfirmPassword(value: String) {
        confirmPasswordValue = value
    }

    private fun validateUsername(): Boolean {
        val username = usernameValue.trim()
        return if (username.isBlank()) {
            usernameError = "Please fill username field"
            false
        } else if (username.length < 6) {
            usernameError = "Username must be at least 5 characters long"
            false
        } else if (username.contains(" ")) {
            usernameError = "Username should not contain spaces"
            false
        } else {
            true
        }
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
    val containsLetter = password.any { it.isLetter() }
    val containsDigit = password.any { it.isDigit() }

    return when {
        password.isBlank() -> {
            passwordError = "Please fill password field"
            false
        }
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

private fun validateConfirmPassword(): Boolean {
    val password = passwordValue.trim()
    val containsLetter = password.any { it.isLetter() }
    val containsDigit = password.any { it.isDigit() }

    return when {
        password.isBlank() -> {
            passwordError = "Please fill password field"
            false
        }
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

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateEmail() && validatePassword() && validateConfirmPassword()) {
            email = emailValue
            password = passwordValue
            onSuccess()
        } else {
            onError("Invalid email or password")
        }
    }

    fun saveUsername(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateUsername()) {
            _loading.value = true
            viewModelScope.launch {
                Log.d("RegisterScreenViewModel", "register: $emailValue, Password : $passwordValue")

                val result = userRepository.register(usernameValue, email, password)
                _loading.value = false
                when (result) {
                    is ResultResponse.Success -> {
                        _isValid.value = true
                        onSuccess()
                    }

                    is ResultResponse.Error -> {
                        _isValid.value = false
                        val errorMessage = result.error ?: "Unknown error occurred"
                        onError(errorMessage)
                    }

                    ResultResponse.Loading -> TODO()
                }
            }
        } else {
            _isValid.value = false
            onError("Invalid username")
        }
    }


}
