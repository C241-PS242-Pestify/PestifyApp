package com.learning.pestifyapp.ui.screen.dashboard.profile

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.ui.common.ResultResponse
import kotlinx.coroutines.launch

class ProfileScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val logoutStatus: MutableLiveData<String> = MutableLiveData()

    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> get() = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isDataLoaded = MutableLiveData(false)
    val isDataLoaded: LiveData<Boolean> get() = _isDataLoaded

    private var usernameValue by mutableStateOf("")
    var usernameError by mutableStateOf("")
        private set

    private var emailValue by mutableStateOf("")
    var emailError by mutableStateOf("")
        private set

    private var passwordValue by mutableStateOf("")
    var passwordError by mutableStateOf("")
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

        if (password.isNotEmpty()) {
            if (password.length < 6) {
                passwordError = "Password must be at least 6 characters long"
                return false
            } else if (!containsLetter) {
                passwordError = "Password must contain at least one letter"
                return false
            } else if (!containsDigit) {
                passwordError = "Password must contain at least one digit"
                return false
            }
        }
        return true
    }

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = userRepository.fetchUserData()) {
                is ResultResponse.Success -> {
                    _userData.value = result.data
                    _isDataLoaded.value = true
                    _isLoading.value = false
                }

                is ResultResponse.Error -> {
                    Log.e("TAG", "FetchAccount error: ${result.error}")
                    ResultResponse.Error(result.error)
                    _isLoading.value = false
                }

                is ResultResponse.Loading -> {
                    Log.d("TAG", "updateAccount loading")
                }
            }
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

        if (!validateUsername() || !validateEmail() || !validatePassword()) {
            return
        }

        viewModelScope.launch {
            Log.d(
                "TAG",
                "updateAccount called with name: $name, email: $email, password: $password"
            )
            when (val result = userRepository.updateAccount(name, email, password)) {
                is ResultResponse.Success -> {
                    _userData.value = result.data
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


    fun logout() {
        viewModelScope.launch {
            when (val result = userRepository.logout()) {
                is ResultResponse.Success -> {
                    logoutStatus.value = result.data
                }

                is ResultResponse.Error -> {
                    logoutStatus.value = result.error
                }

                ResultResponse.Loading -> TODO()
            }
        }
    }
}