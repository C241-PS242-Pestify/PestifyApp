package com.learning.pestifyapp.di

import android.content.Context
import com.learning.pestifyapp.data.model.local.room.AppDatabase
import com.learning.pestifyapp.data.repository.EnsiklopediaRepository
import com.learning.pestifyapp.data.repository.HomeRepository
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

        return HomeRepository.getInstance(plantService,articleService, plantTable, articleTable)
    }

    fun providePespediaRepository(): EnsiklopediaRepository {
        return EnsiklopediaRepository.getInstance()
    }
}
