package com.learning.pestifyapp.ui.screen.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.model.plant.PlantData
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.repository.HomeRepository
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val userRepository = UserRepository.getInstance(MainActivity.CONTEXT)

    private val _uiState: MutableStateFlow<UiState<List<PlantData>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<PlantData>>> = _uiState.asStateFlow()
    private val _user = MutableLiveData<UserData?>()
    val user: LiveData<UserData?> = _user

    private val _isLoggedIn = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            _isLoggedIn.value = userRepository.getLoginStatus()
            if (_isLoggedIn.value == true) {
                _user.value = userRepository.getUserSession()
            } else {
                _user.value = null
            }
        }

    }

    fun getAllPlants() {
        viewModelScope.launch {
            homeRepository.getAllPlants()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { plantList ->
                    _uiState.value = UiState.Success(plantList)
                }
        }
    }
}