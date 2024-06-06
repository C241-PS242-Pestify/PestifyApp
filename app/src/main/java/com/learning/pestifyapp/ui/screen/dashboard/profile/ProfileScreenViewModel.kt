package com.learning.pestifyapp.ui.screen.dashboard.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.data.response.ResultResponse
import kotlinx.coroutines.launch

class ProfileScreenViewModel(private val userRepository: UserRepository) : ViewModel() {

    val logoutStatus: MutableLiveData<String> = MutableLiveData()

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