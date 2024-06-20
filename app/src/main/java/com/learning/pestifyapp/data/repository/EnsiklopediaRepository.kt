package com.learning.pestifyapp.data.repository

import android.util.Log
import com.learning.pestifyapp.data.model.local.dao.PespediaDao
import com.learning.pestifyapp.data.model.local.entity.PespediaEntity
import com.learning.pestifyapp.data.retrofit.service.PespediaService
import com.learning.pestifyapp.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EnsiklopediaRepository(
    private val apiService: PespediaService,
    private val pespediaDao: PespediaDao
) {

    init {
        getAllEnsArticles()
    }

    fun getEnsArticleById(ensId: String): Flow<UiState<PespediaEntity>> = flow {
        val localData = pespediaDao.getPespediaById(ensId)
        if (localData != null) {
            emit(UiState.Success(localData))
        } else {
            try {
                emit(UiState.Loading)
                val pespediaItem = apiService.getPespediaById(ensId)
                val pespediaEntity = PespediaEntity(
                    id = pespediaItem.id,
                    title = pespediaItem.title,
                    description = pespediaItem.description,
                    additionalDescription1 = pespediaItem.additionalDescription1,
                    additionalDescription2 = pespediaItem.additionalDescription2,
                    picture = pespediaItem.picture
                )
                if (pespediaEntity != null) {
                    pespediaDao.insert(pespediaEntity)
                    emit(UiState.Success(pespediaEntity))
                } else {
                    emit(UiState.Error("Article not found"))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }
    }

    fun getAllEnsArticles(): Flow<UiState<List<PespediaEntity>>> = flow {
        Log.d("EnsiklopediaRepository", "getAllEnsArticles: ")
//
        val localData = pespediaDao.getAllPespedia()
        Log.d("EnsiklopediaRepository", "getAllEnsArticles: $localData")
        if (localData.isNotEmpty()) {
            emit(UiState.Success(localData))
        } else {
            try {
                val response = apiService.getAllPespedia()
                Log.d("EnsiklopediaRepository", "getAllEnsArticles: API data found $response items")
                val list = response.map { item ->
                    PespediaEntity(
                        id = item.id,
                        title = item.title,
                        description = item.description,
                        additionalDescription1 = item.additionalDescription1,
                        additionalDescription2 = item.additionalDescription2,
                        picture = item.picture
                    )
                }
                pespediaDao.deleteAllPespedia()
                pespediaDao.insertAll(list)
                emit(UiState.Success(list))
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))

            }
        }
    }

    fun searchEnsItem(query: String): Flow<UiState<List<PespediaEntity>>> = flow {
        val localData = pespediaDao.getAllPespedia()
        if (localData.isNotEmpty()) {
            val ensList = localData.filter {
                it.title.contains(query, ignoreCase = true)
            }
            emit(UiState.Success(ensList))
        } else {
            emit(UiState.Loading)
            try {
                val response = apiService.searchPespedia(query)
                val list = response.map {
                    PespediaEntity(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        additionalDescription1 = it.additionalDescription1,
                        additionalDescription2 = it.additionalDescription2,
                        picture = it.picture
                    )

                }
                pespediaDao.deleteAllPespedia()
                pespediaDao.insertAll(list)
                emit(UiState.Success(list))
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }
    }

    fun onSearch(query: String) : Flow<UiState<List<PespediaEntity>>> = flow {
        try {
            val response = apiService.searchPespedia(query)
            val list = response.map {
                PespediaEntity (
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    additionalDescription1 = it.additionalDescription1,
                    additionalDescription2 = it.additionalDescription2,
                    picture = it.picture
                )
            }
            pespediaDao.deleteAllPespedia()
            pespediaDao.insertAll(list)
            emit(UiState.Success(list))
        } catch (e: Exception) {
            emit(UiState.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: EnsiklopediaRepository? = null

        @JvmStatic
        fun getInstance(
            apiService: PespediaService,
            pespediaDao: PespediaDao
        ): EnsiklopediaRepository =
            INSTANCE ?: synchronized(this) {
                EnsiklopediaRepository(
                    apiService = apiService,
                    pespediaDao = pespediaDao
                ).apply {
                    INSTANCE = this
                }
            }
    }
}