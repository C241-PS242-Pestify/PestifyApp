package com.learning.pestifyapp.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.pestifyapp.di.Injection
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaViewModel
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel

class PespediaFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EnsiklopediaViewModel::class.java) -> {
                EnsiklopediaViewModel(Injection.providePespediaRepository()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PespediaFactory? = null

        @JvmStatic
        fun getInstance(): PespediaFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PespediaFactory()
            }.also { INSTANCE = it }
    }
}