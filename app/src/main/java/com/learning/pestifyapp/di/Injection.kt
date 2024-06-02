package com.learning.pestifyapp.di

import android.content.Context
import com.learning.pestifyapp.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        return UserRepository(context)
    }
}
