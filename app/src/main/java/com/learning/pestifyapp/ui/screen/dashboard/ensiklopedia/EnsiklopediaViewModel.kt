package com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.model.ensdata.Ensiklopedia
import com.learning.pestifyapp.data.model.plant.PlantData
import com.learning.pestifyapp.data.repository.EnsiklopediaRepository
import com.learning.pestifyapp.ui.common.UiState
import com.learning.pestifyapp.ui.screen.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EnsiklopediaViewModel(private val repository: EnsiklopediaRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<Ensiklopedia>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Ensiklopedia>>> = _uiState.asStateFlow()

    private val _uiStateEns : MutableStateFlow<UiState<Ensiklopedia>> = MutableStateFlow(UiState.Loading)
    val uiStateEns: StateFlow<UiState<Ensiklopedia>> = _uiStateEns.asStateFlow()

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        viewModelScope.launch {
            repository.searchHeroes(newQuery)
                .catch {
                    _query.value = newQuery
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect {ensList ->
                    _query.value = newQuery
                    _uiState.value = ensList
                }
        }
    }

    fun getAllEnsArticles() {
        viewModelScope.launch {
            repository.getAllEnsArticles()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { ensList ->
                    _uiState.value = ensList
                }
        }
    }

    fun getEnsArticleById(ensId: String) {
        viewModelScope.launch {
            repository.getEnsArticleById(ensId)
                .catch {
                    _uiStateEns.value = UiState.Error(it.message.toString())
                }
                .collect { ensId ->
                    _uiStateEns.value = ensId
                }
        }
    }

}