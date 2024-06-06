package com.learning.pestifyapp.ui.screen.authentication.login

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
import com.learning.pestifyapp.data.response.ResultResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val _isValid = MutableLiveData(false)
    val isValid: LiveData<Boolean> = _isValid

    var emailValue by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set

    fun setEmail(value: String) {
        emailValue = value
    }

    var passwordValue by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set

    fun setPassword(value: String) {
        passwordValue = value
    }

    private fun validateEmail(): Boolean {
        val email = emailValue.trim()
        var isValid = true
        if (email.isEmpty() || email.isBlank()) {
            emailError = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email address"
            isValid = false
        }
        return isValid
    }

    private fun validatePassword(): Boolean {
        val password = passwordValue.trim()
        var isValid = true
        if (password.isBlank() || password.isEmpty()) {
            passwordError = "Please fill password field"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        }
        return isValid
    }


    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateEmail() && validatePassword()) {
            _loading.value = true
            viewModelScope.launch {
                val result = userRepository.login(emailValue, passwordValue)
                _loading.value = false
                when (result) {
                    is ResultResponse.Success -> {
                        _isValid.value = true
                        userRepository.saveToken(result.data.token!!)
                        userRepository.saveLoginStatus(true)
                        userRepository.saveEmail(emailValue)
                        val userSession = UserData(result.data.token, emailValue, "", isLogin = true)
                        userRepository.saveUserSession(userSession)
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
            onError("Invalid email or password")
        }
    }

}