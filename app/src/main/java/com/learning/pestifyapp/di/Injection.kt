package com.learning.pestifyapp.di

import android.content.Context
import com.learning.pestifyapp.data.repository.HomeRepository
import com.learning.pestifyapp.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        return UserRepository.getInstance(context)
    }

    fun provideHomeRepository(): HomeRepository {
        return HomeRepository.getInstance()
    }
}
