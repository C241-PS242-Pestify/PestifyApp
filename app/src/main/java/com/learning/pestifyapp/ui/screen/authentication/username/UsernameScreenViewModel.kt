package com.learning.pestifyapp.ui.screen.authentication.username

import com.learning.pestifyapp.data.UserRepository
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

    var usernameValue by mutableStateOf("")
        private set
    var usernameError by mutableStateOf("")
        private set

    fun setUsername(value: String) {
        usernameValue = value
    }

    fun validateUsername(): Boolean {
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

    fun saveUsername() {
        _loading.value = true
        viewModelScope.launch {
            try {
                userRepository.setUsername(usernameValue)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to save username"
            } finally {
                _loading.value = false
            }
        }
    }
}
