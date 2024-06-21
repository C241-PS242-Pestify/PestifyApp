package com.learning.pestifyapp.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.di.Injection
import com.learning.pestifyapp.ui.screen.authentication.forgotpassword.ForgotPasswordScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.login.LoginScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.register.RegisterScreenViewModel
import com.learning.pestifyapp.ui.screen.dashboard.profile.ProfileViewModel

class UserFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginScreenViewModel::class.java) -> {
                LoginScreenViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterScreenViewModel::class.java) -> {
                RegisterScreenViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ForgotPasswordScreenViewModel::class.java) -> {
                ForgotPasswordScreenViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserFactory? = null

        @JvmStatic
        fun getInstance(context: Context): UserFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserFactory(
                    Injection.provideUserRepository(context),)
            }.also { INSTANCE = it }
    }
}