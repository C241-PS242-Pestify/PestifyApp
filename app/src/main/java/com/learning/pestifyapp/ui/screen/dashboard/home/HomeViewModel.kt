package com.learning.pestifyapp.ui.screen.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.model.local.entity.ArticleEntity
import com.learning.pestifyapp.data.model.local.entity.PlantEntity
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.repository.HomeRepository
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val userRepository = UserRepository.getInstance(MainActivity.CONTEXT)

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)


    private val _uiListPlantState: MutableStateFlow<UiState<List<PlantEntity>>> =
        MutableStateFlow(UiState.Loading)
    val uiListPlantState: StateFlow<UiState<List<PlantEntity>>> = _uiListPlantState.asStateFlow()

    private val _uiListArticleState: MutableStateFlow<UiState<List<ArticleEntity>>> =
        MutableStateFlow(UiState.Loading)
    val uiListArticleState: StateFlow<UiState<List<ArticleEntity>>> =
        _uiListArticleState.asStateFlow()

    private val _uiPlantState: MutableStateFlow<UiState<PlantEntity>> =
        MutableStateFlow(UiState.Loading)
    val uiPlantState: StateFlow<UiState<PlantEntity>> = _uiPlantState.asStateFlow()

    private val _uiArticle: MutableStateFlow<UiState<ArticleEntity>> =
        MutableStateFlow(UiState.Loading)
    val uiArticle: StateFlow<UiState<ArticleEntity>> = _uiArticle.asStateFlow()

    private val _selectedCategory: MutableStateFlow<String> = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()


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
            homeRepository.getAllArticles()
                .catch {
                    _uiListArticleState.value = UiState.Error(it.message.toString())
                }
                .collect { articleList ->
                    _uiListArticleState.value = articleList
                }
        }
    }

    fun getArticleById(articleId: String) {
        viewModelScope.launch {
            homeRepository.getArticleById(articleId)
                .catch {
                    _uiArticle.value = UiState.Error(it.message.toString())
                }
                .collect { article ->
                    _uiArticle.value = article
                }
        }
    }

    fun filterArticles(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            if (category == "All") {
                homeRepository.getAllArticles()
                    .catch {
                        _uiListArticleState.value = UiState.Error(it.message.toString())
                    }
                    .collect { uiState ->
                        _uiListArticleState.value = uiState
                    }
            } else {
                homeRepository.getArticlesByTag(category)
                    .catch {
                        _uiListArticleState.value = UiState.Error(it.message.toString())
                    }
                    .collect { uiState ->
                        _uiListArticleState.value = uiState
                    }
            }
        }
    }


    fun getAllPlants() {
        viewModelScope.launch {
            homeRepository.getAllPlants()
                .catch {
                    _uiListPlantState.value = UiState.Error(it.message.toString())
                }
                .collect { plantList ->
                    _uiListPlantState.value = plantList
                }
        }
    }

    fun getPlantById(plantId: String) {
        viewModelScope.launch {
            homeRepository.getPlantById(plantId)
                .catch {
                    _uiPlantState.value = UiState.Error(it.message.toString())
                }
                .collect { plant ->
                    _uiPlantState.value = plant
                }
        }
    }
}