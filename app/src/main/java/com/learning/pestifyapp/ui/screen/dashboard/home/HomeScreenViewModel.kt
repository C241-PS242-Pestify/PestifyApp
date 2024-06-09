package com.learning.pestifyapp.ui.screen.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<UserData?>()
    val user: LiveData<UserData?> = _user

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        viewModelScope.launch {
            _isLoggedIn.value = userRepository.getLoginStatus()
            if (_isLoggedIn.value == true) {
                _user.value = userRepository.getUserSession()
            }
        }
    }

//    fun logout() {
//        userRepository.logout()
//        _isLoggedIn.value = false
//        _user.value = null
//    }

//    fun fetchUsername(): String {
//        return _user.value?.name ?: ""
//    }

}