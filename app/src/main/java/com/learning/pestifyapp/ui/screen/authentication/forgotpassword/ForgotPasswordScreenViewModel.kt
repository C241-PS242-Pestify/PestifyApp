package com.learning.pestifyapp.ui.screen.authentication.forgotpassword

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.learning.pestifyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class ForgotPasswordScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    var emailValue by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set

    fun setEmail(value: String) {
        emailValue = value
    }

    private fun validateEmail(): Boolean {
        val email = emailValue.trim()
        var isvalid = true
        if (email.isEmpty() || email.isBlank()) {
            emailError = "Email is required"
            isvalid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email address"
            isvalid = false
        }
        return isvalid
    }

    fun resetPassword(onSuccess: () -> Unit, onError: (String) -> Unit) {
//        if (validateEmail()) {
//            _loading.value = true
//            viewModelScope.launch {
//                val result = userRepository.resetPassword(emailValue)
//                result.onSuccess {
//                    onSuccess.invoke()
//                    _loading.value = false
//                }
//                result.onFailure {
//                    onError(it.message ?: "An error occurred")
//                    _loading.value = false
//                }
//            }
//        }
    }
}
