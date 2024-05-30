package com.learning.pestifyapp.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.pestifyapp.data.UserRepository
import com.learning.pestifyapp.di.Injection
import com.learning.pestifyapp.ui.screen.authentication.forgotpassword.ForgotPasswordScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.login.LoginScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.register.RegisterScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.username.UsernameScreenViewModel
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeScreenViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> {
                HomeScreenViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginScreenViewModel::class.java) -> {
                LoginScreenViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterScreenViewModel::class.java) -> {
                RegisterScreenViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(UsernameScreenViewModel::class.java) -> {
                UsernameScreenViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ForgotPasswordScreenViewModel::class.java) -> {
                ForgotPasswordScreenViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
//        fun getInstance(context: Context)= ViewModelFactory(Injection.provideRepository(context))
    }
}
