package com.learning.pestifyapp.di

import android.content.Context
import com.learning.pestifyapp.data.UserRepository
import kotlinx.coroutines.runBlocking

object Injection {

//    fun provideRepository(context: Context): UserRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//
//        val apiService = ApiConfig.getApiService(user.token)
//        return UserRepository.getInstance(apiService, pref)
//    }
}