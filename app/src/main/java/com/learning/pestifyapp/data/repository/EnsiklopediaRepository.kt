package com.learning.pestifyapp.data.repository

import com.learning.pestifyapp.data.model.ensdata.Ensiklopedia
import com.learning.pestifyapp.data.model.ensdata.EnsiklopediaData
import com.learning.pestifyapp.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EnsiklopediaRepository {

    private val ensList = mutableListOf<Ensiklopedia>()

    init {
        if (ensList.isEmpty()) {
            ensList.addAll(EnsiklopediaData.ensiklopediaList)
        }
    }

    fun getEnsArticleById(ensId: String): Flow<UiState<Ensiklopedia>> = flow {
        emit(UiState.Loading)
        try {
            emit(
                UiState.Success(
                    ensList.first {
                        it.id == ensId
                    }
                )
            )
        } catch (e: Exception) {
            emit(UiState.Error(e.message.toString()))
        }
    }

    fun getAllEnsArticles(): Flow<UiState<List<Ensiklopedia>>> = flow {
        emit(UiState.Loading)
        try {
            emit(UiState.Success(ensList))
        } catch (e: Exception) {
            emit(UiState.Error(e.message.toString()))
        }
    }

    fun searchHeroes(query: String): Flow<UiState<List<Ensiklopedia>>> = flow {
        emit(UiState.Loading)
        try {
            emit(
                UiState.Success(
                    ensList.filter {
                        it.title.contains(query, ignoreCase = true)
                    }
                )
            )
        } catch (e: Exception) {
            emit(UiState.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: EnsiklopediaRepository? = null

        @JvmStatic
        fun getInstance(): EnsiklopediaRepository =
            INSTANCE ?: synchronized(this) {
                EnsiklopediaRepository().apply {
                    INSTANCE = this
                }
            }
    }
}