package com.learning.pestifyapp.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.pestifyapp.data.repository.EnsiklopediaRepository
import com.learning.pestifyapp.data.repository.HistoryRepository
import com.learning.pestifyapp.data.repository.PestRepository
import com.learning.pestifyapp.di.Injection
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaViewModel
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PestViewModel

class PestFactory private constructor(
    private val pestRepository: PestRepository,
    private val historyRepository: HistoryRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PestViewModel::class.java) -> {
                PestViewModel(pestRepository, historyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PestFactory? = null

        @JvmStatic
        fun getInstance(context: Context): PestFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PestFactory(
                    Injection.providePestRepository(context),
                    Injection.provideHistoryRepository(context)
                )
            }.also { INSTANCE = it }
    }
}