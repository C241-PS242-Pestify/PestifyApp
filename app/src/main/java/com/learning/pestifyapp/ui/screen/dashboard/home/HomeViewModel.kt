package com.learning.pestifyapp.ui.screen.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.model.plant.PlantData
import com.learning.pestifyapp.data.repository.HomeRepository
import com.learning.pestifyapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val HomeRepository: HomeRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<PlantData>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<PlantData>>> = _uiState.asStateFlow()

    fun getAllPlants() {
        viewModelScope.launch {
            HomeRepository.getAllPlants()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { plantList ->
                    _uiState.value = UiState.Success(plantList)
                }
        }
    }
}