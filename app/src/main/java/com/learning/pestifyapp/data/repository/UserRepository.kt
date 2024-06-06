package com.learning.pestifyapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.response.LoginResponse
import com.learning.pestifyapp.data.response.RegisterResponse
import com.learning.pestifyapp.data.response.ResultResponse
import com.learning.pestifyapp.data.retrofit.ApiConfig
import com.learning.pestifyapp.data.retrofit.AuthService
import com.learning.pestifyapp.data.retrofit.LoginRequest
import com.learning.pestifyapp.data.retrofit.RegisterRequest

class UserRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val authService: AuthService = ApiConfig.getAuthService()

    suspend fun register(
        name: String,
        email: String,
        password: String,
    ): ResultResponse<RegisterResponse> {
        return try {
            val request = RegisterRequest(name, email, password)
            val response = authService.register(request)
            ResultResponse.Success(response)
        } catch (e: Exception) {
            Log.e("AuthRepository", "register: ${e.message}", e)
            ResultResponse.Error(e.message.toString())
        }
    }

    suspend fun login(email: String, password: String): ResultResponse<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = authService.login(request)
            ResultResponse.Success(response)
        } catch (e: Exception) {
            Log.e("AuthRepository", "login: ${e.message}", e)
            ResultResponse.Error(e.message.toString())
        }
    }

    suspend fun logout(): ResultResponse<String> {
        val token = getToken() ?: return ResultResponse.Error("User is not logged in")
        return try {
            val response = authService.logout("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                clearSession()
                ResultResponse.Success(response.body()!!.message ?: "Logout successful")
            } else {
                ResultResponse.Error("Logout failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "logout: ${e.message}", e)
            ResultResponse.Error(e.message.toString())
        }
    }
    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun saveLoginStatus(isLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLogin", isLogin)
        editor.apply()
    }

    fun getLoginStatus(): Boolean {
        return sharedPreferences.getBoolean("isLogin", false)
    }

    fun saveEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun saveUserSession(userSession: UserData) {
        val editor = sharedPreferences.edit()
        editor.putString("token", userSession.token)
        editor.putString("email", userSession.email)
        editor.putString("username", userSession.username)
        editor.putBoolean("isLogin", userSession.isLogin)
        editor.apply()
    }

    fun getUserSession(): UserData? {
        if (!getLoginStatus()) {
            return null
        }
        val token = getToken()
        val email = sharedPreferences.getString("email", null)
        val username = sharedPreferences.getString("username", null)
        val isLogin = getLoginStatus()
        return UserData(token, email, username, isLogin)
    }

    private fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        @JvmStatic
        fun getInstance(context: Context): UserRepository =
            INSTANCE ?: synchronized(this) {
                UserRepository(context).apply {
                    INSTANCE = this
                }
            }
    }
}