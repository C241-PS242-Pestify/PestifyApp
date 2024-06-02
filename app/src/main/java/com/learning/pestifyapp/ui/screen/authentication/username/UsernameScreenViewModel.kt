package com.learning.pestifyapp.ui.screen.authentication.username

import com.learning.pestifyapp.data.repository.UserRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UsernameScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isValid = MutableLiveData(false)
    val isValid: LiveData<Boolean> = _isValid
    var usernameValue by mutableStateOf("")
        private set
    var usernameError by mutableStateOf("")
        private set

    fun setUsername(value: String) {
        usernameValue = value
    }

    private fun validateUsername(): Boolean {
        val username = usernameValue.trim()
        return if (username.isBlank()) {
            usernameError = "Please fill username field"
            false
        } else if (username.length < 6) {
            usernameError = "Username must be at least 6 characters long"
            false
        } else if (username.contains(" ")) {
            usernameError = "Username should not contain spaces"
            false
        } else {
            true
        }
    }

    fun saveUsername(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateUsername()) {
            _loading.value = true
            viewModelScope.launch {
                val result = userRepository.setUsername(usernameValue)
                result.onSuccess {
                    _isValid.value = true
                    onSuccess.invoke()
                }.onFailure {
                    onError.invoke(it.message ?: "Unknown error occurred")
                }
                _loading.value = false
            }
        } else {
            _isValid.value = false
            onError.invoke("Invalid username")
        }
    }
}
