package com.learning.pestifyapp.data.repository

import android.content.Context
import android.util.Log
import com.learning.pestifyapp.data.model.homeart.Article
import com.learning.pestifyapp.data.model.homeart.FakeArtData
import com.learning.pestifyapp.data.model.local.dao.ArticleDao
import com.learning.pestifyapp.data.model.local.dao.PlantDao
import com.learning.pestifyapp.data.model.local.entity.ArticleEntity
import com.learning.pestifyapp.data.model.local.entity.PlantEntity
import com.learning.pestifyapp.data.model.plant.FakePlantData
import com.learning.pestifyapp.data.model.plant.PlantData
import com.learning.pestifyapp.data.retrofit.service.ArticleService
import com.learning.pestifyapp.data.retrofit.service.PlantService
import com.learning.pestifyapp.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class HomeRepository private constructor(
    private val plantService: PlantService,
    private val articleService: ArticleService,
    private val plantDao: PlantDao,
    private val articleDao: ArticleDao
) {

    init {
        getAllPlants()
        getAllArticles()
    }

    fun getAllArticles(): Flow<UiState<List<ArticleEntity>>> = flow {

        val localData = articleDao.getAllArticles()
        if (localData.isNotEmpty()) {
            emit(UiState.Success(localData))
        } else {

            try {
                emit(UiState.Loading)
                val articleList = articleService.getAllArticles().map { item ->
                    ArticleEntity(
                        id = item.id,
                        title = item.title,
                        tags = item.tags,
                        description = item.description,
                        picture = item.picture,
                        createdAt = item.createdAt
                    )
                }
                articleDao.deleteAllArticles()
                articleDao.insertAll(articleList)
                emit(UiState.Success(articleList))
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }

    }

    fun getArticleById(articleId: String): Flow<UiState<ArticleEntity>> = flow {

        val localArticle = articleDao.getArticleById(articleId)
        if (localArticle != null) {
            emit(UiState.Success(localArticle))
        } else {
            emit(UiState.Loading)
            try {
                val articleItem = articleService.getArticleById(articleId)
                val articleEntity = ArticleEntity(
                    id = articleItem.id,
                    title = articleItem.title,
                    tags = articleItem.tags,
                    description = articleItem.description,
                    picture = articleItem.picture,
                    createdAt = articleItem.createdAt
                )

                if (articleEntity != null) {
                    articleDao.insert(articleEntity)
                    emit(UiState.Success(articleEntity))
                } else {
                    emit(UiState.Error("Article not found"))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }
    }

    fun getArticlesByTag(tag: String): Flow<UiState<List<ArticleEntity>>> = flow {
        val articles = articleDao.getAllArticles()
        if (articles.isNotEmpty()) {
            val filteredArticles = if (tag == "All") {
                articles
            } else {
                articles.filter { it.tags.split(",").contains(tag) }
            }
            emit(UiState.Success(filteredArticles))
        } else {
            emit(UiState.Loading)
            try {
                val response = articleService.getArticlesByTag(tag)
                if (response.isNotEmpty()) {
                    val list = response.map { item ->
                        ArticleEntity(
                            id = item.id,
                            title = item.title,
                            tags = item.tags,
                            description = item.description,
                            picture = item.picture,
                            createdAt = item.createdAt
                        )
                    }
                    articleDao.insertAll(list)
                    emit(UiState.Success(list))
                } else {
                    emit(UiState.Error("No articles found for this tag"))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }
    }

    fun getAllPlants(): Flow<UiState<List<PlantEntity>>> = flow {

        val localData = plantDao.getAllPlants()
        if (localData.isNotEmpty()) {
            emit(UiState.Success(localData))
        } else {
            try {
                emit(UiState.Loading)
                val response = plantService.getAllPlants()
                Log.d("HomeRepository", "getAllPlants: API data found $response items")
                val plantList = response.map { item ->
                    PlantEntity(
                        id = item.id,
                        description = item.description,
                        title = item.title,
                        picture = item.picture
                    )
                }

                plantDao.deleteAllPlants()
                plantDao.insertAll(plantList)
                emit(UiState.Success(plantList))
            } catch (e: Exception) {
                Log.e("HomeRepository", "getAllPlants: ${e.message}", e)
                emit(UiState.Error(e.message.toString()))
            }
        }
    }

    fun getPlantById(plantId: String): Flow<UiState<PlantEntity>> = flow {

        val localPlant = plantDao.getPlantById(plantId)
        if (localPlant != null) {
            emit(UiState.Success(localPlant))
        } else {
            emit(UiState.Loading)
            try {
                val response = plantService.getAllPlants()
                val plantList = response.map { item ->
                    PlantEntity(
                        id = item.id,
                        description = item.description,
                        title = item.title,
                        picture = item.picture
                    )
                }

                plantDao.deleteAllPlants()
                plantDao.insertAll(plantList)

                val plant = plantList.first { it.id == plantId }
                emit(UiState.Success(plant))
            } catch (e: Exception) {
                if (localPlant == null) {
                    emit(UiState.Error(e.message.toString()))
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeRepository? = null

        @JvmStatic
        fun getInstance(
            plantService: PlantService,
            articleService: ArticleService,
            plantDao: PlantDao,
            articleDao: ArticleDao
        ): HomeRepository =
            INSTANCE ?: synchronized(this) {
                HomeRepository(
                    plantService = plantService,
                    articleService = articleService,
                    plantDao = plantDao,
                    articleDao = articleDao
                ).apply {
                    INSTANCE = this
                }
            }
    }
}