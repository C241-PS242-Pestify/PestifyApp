package com.learning.pestifyapp.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.pestifyapp.data.repository.HomeRepository
import com.learning.pestifyapp.di.Injection
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel

class HomeFactory private constructor(private val homeRepository: HomeRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(homeRepository = homeRepository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }

        companion object {
            @Volatile
            private var INSTANCE: HomeFactory? = null

            @JvmStatic
            fun getInstance(context: Context): HomeFactory =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: HomeFactory(Injection.provideHomeRepository(context = context))
                }.also { INSTANCE = it }
        }
}