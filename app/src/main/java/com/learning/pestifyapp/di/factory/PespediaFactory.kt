package com.learning.pestifyapp.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.pestifyapp.data.repository.EnsiklopediaRepository
import com.learning.pestifyapp.di.Injection
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaViewModel
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel

class PespediaFactory private constructor(private val ensiklopediaRepository: EnsiklopediaRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EnsiklopediaViewModel::class.java) -> {
                EnsiklopediaViewModel(ensiklopediaRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PespediaFactory? = null

        @JvmStatic
        fun getInstance(context: Context): PespediaFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PespediaFactory(Injection.providePespediaRepository(context))
            }.also { INSTANCE = it }
    }
}