package com.learning.pestifyapp.ui.screen.dashboard.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.data.response.ResultResponse
import kotlinx.coroutines.launch

class ProfileScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val logoutStatus: MutableLiveData<String> = MutableLiveData()

    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> get() = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isDataLoaded = MutableLiveData<Boolean>(false)
    val isDataLoaded: LiveData<Boolean> get() = _isDataLoaded

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            _isLoading.value = true // Set isLoading true saat memulai pengambilan data
            when (val result = userRepository.fetchUserData()) {
                is ResultResponse.Success -> {
                    _userData.value = result.data
                    _isDataLoaded.value = true // Set isDataLoaded true setelah data berhasil dimuat
                    _isLoading.value = false // Set isLoading false setelah data berhasil diambil
                }
                is ResultResponse.Error -> {
                    Log.e("TAG", "FetchAccount error: ${result.error}")
                    ResultResponse.Error(result.error)
                    _isLoading.value = false // Set isLoading false jika terjadi kesalahan saat mengambil data
                }
                ResultResponse.Loading -> {
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
        viewModelScope.launch {
            Log.d("TAG", "updateAccount called with name: $name, email: $email, password: $password")
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