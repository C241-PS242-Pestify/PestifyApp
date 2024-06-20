package com.learning.pestifyapp.di

import android.content.Context
import com.learning.pestifyapp.data.model.local.room.AppDatabase
import com.learning.pestifyapp.data.repository.EnsiklopediaRepository
import com.learning.pestifyapp.data.repository.HistoryRepository
import com.learning.pestifyapp.data.repository.HomeRepository
import com.learning.pestifyapp.data.repository.PestRepository
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.data.retrofit.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        return UserRepository.getInstance(context)
    }

    fun provideHomeRepository(context: Context): HomeRepository {
        val plantService = ApiConfig.getPlantService(context)
        val articleService = ApiConfig.getArticleService(context)
        val plantTable = AppDatabase.getInstance(context).plantDao()
        val articleTable = AppDatabase.getInstance(context).articleDao()

        return HomeRepository.getInstance(plantService, articleService, plantTable, articleTable)
    }

    fun providePespediaRepository(context: Context): EnsiklopediaRepository {
        val pespediaService = ApiConfig.getPespediaService(context)
        val pespediaTable = AppDatabase.getInstance(context).pespediaDao()

        return EnsiklopediaRepository.getInstance(pespediaService, pespediaTable)
    }

    fun providePestRepository(context: Context): PestRepository {
        return PestRepository.getInstance(context)
    }
    fun provideHistoryRepository(context: Context): HistoryRepository {
        val historyTable = AppDatabase.getInstance(context).historyImageDao()
        val historyService = ApiConfig.getHistoryServie(context)

        return HistoryRepository.getInstance(historyTable, historyService)
    }
}
