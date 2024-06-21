package com.learning.pestifyapp.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.pestifyapp.data.repository.HistoryRepository
import com.learning.pestifyapp.di.Injection
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryViewModel

class HistoryFactory private constructor(private val historyRepository: HistoryRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(historyRepository = historyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HistoryFactory? = null

        @JvmStatic
        fun getInstance(context: Context): HistoryFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HistoryFactory(Injection.provideHistoryRepository(context = context))
            }.also { INSTANCE = it }
    }
}