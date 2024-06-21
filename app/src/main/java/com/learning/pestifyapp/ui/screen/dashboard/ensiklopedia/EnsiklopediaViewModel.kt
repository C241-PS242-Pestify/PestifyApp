package com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.model.local.entity.PespediaEntity
import com.learning.pestifyapp.data.repository.EnsiklopediaRepository
import com.learning.pestifyapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EnsiklopediaViewModel(private val repository: EnsiklopediaRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<PespediaEntity>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<PespediaEntity>>> = _uiState.asStateFlow()

    private val _uiStateEns: MutableStateFlow<UiState<PespediaEntity>> =
        MutableStateFlow(UiState.Loading)
    val uiStateEns: StateFlow<UiState<PespediaEntity>> = _uiStateEns.asStateFlow()

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        _query.value = newQuery // Cek disini
        viewModelScope.launch {
            repository.searchEnsItem(newQuery)
                .catch {
                    Log.d("Search", "Query: $newQuery")
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { ensList ->
                    Log.d("Search", "Query: $newQuery")
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

    fun onSearch(query: String) {
        viewModelScope.launch {
            repository.searchEnsItem(query)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { ensList ->
                    _uiState.value = ensList
                }
        }
    }

}