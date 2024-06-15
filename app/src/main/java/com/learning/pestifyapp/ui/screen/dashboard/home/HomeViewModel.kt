package com.learning.pestifyapp.ui.screen.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.model.homeart.Article
import com.learning.pestifyapp.data.model.homeart.FakeArtData
import com.learning.pestifyapp.data.model.plant.PlantData
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.repository.HomeRepository
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val userRepository = UserRepository.getInstance(MainActivity.CONTEXT)

    private val _uiState: MutableStateFlow<UiState<List<PlantData>>> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<List<PlantData>>> = _uiState.asStateFlow()

    private val _uiArticleState: MutableStateFlow<UiState<List<Article>>> =
        MutableStateFlow(UiState.Loading)

    val uiArticleState: StateFlow<UiState<List<Article>>> = _uiArticleState.asStateFlow()

    private val _selectedCategory: MutableStateFlow<String> = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

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

    fun getAllArticles() {
        viewModelScope.launch {
            homeRepository.getAllArticles(_selectedCategory.value)
                .catch {
                    _uiArticleState.value = UiState.Error(it.message.toString())
                }
                .collect { articleList ->
                    _uiArticleState.value = UiState.Success(articleList)
                }
        }
    }

    fun filterArticles(category: String) {
        _selectedCategory.value = category
        _uiArticleState.value = if (category == "All") {
            UiState.Success(FakeArtData.dummyArticles)
        } else {
            UiState.Success(FakeArtData.dummyArticles.filter { it.category == category })
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