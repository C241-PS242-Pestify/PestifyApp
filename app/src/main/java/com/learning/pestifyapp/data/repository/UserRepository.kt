package com.learning.pestifyapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.response.AccountUpdateResponse
import com.learning.pestifyapp.data.response.LoginResponse
import com.learning.pestifyapp.data.response.RegisterResponse
import com.learning.pestifyapp.ui.common.ResultResponse
import com.learning.pestifyapp.data.retrofit.ApiConfig
import com.learning.pestifyapp.data.retrofit.AuthService
import com.learning.pestifyapp.data.retrofit.LoginRequest
import com.learning.pestifyapp.data.retrofit.AccountService
import com.learning.pestifyapp.data.retrofit.RegisterRequest
import com.learning.pestifyapp.data.retrofit.UpdateAccountRequest
import retrofit2.Response

class UserRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val authService: AuthService = ApiConfig.getAuthService()
    private val accountService: AccountService = ApiConfig.getPageService()


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

    suspend fun fetchUserData(): ResultResponse<UserData> {
        val token = getToken() ?: return ResultResponse.Error("User is not logged in")
        return try {
            val response = accountService.fetchUserData("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!.user
                val userData = UserData(token, user?.email, user?.username, true)
                saveUserSession(userData)
                ResultResponse.Success(userData)
            } else {
                ResultResponse.Error("Fetch user data failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "fetchUserData: ${e.message}", e)
            ResultResponse.Error(e.message.toString())
        }
    }

    suspend fun updateAccount(
        name: String?,
        email: String?,
        password: String?,
    ): ResultResponse<UserData> {
        val token = getToken() ?: return ResultResponse.Error("User is not logged in")
        return try {
            Log.d("AuthRepository", "updateAccount request with name: $name, email: $email, password: $password")
            val request = UpdateAccountRequest(name, email, password)
            val response: Response<AccountUpdateResponse> =
                authService.updateAccount("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!.updatedUser
                if (user != null) {
                    val userData = UserData(token, user.email, user.username, true)
                    saveUserSession(userData)
                    Log.d("AuthRepository", "updateAccount success: $userData")
                    ResultResponse.Success(userData)
                } else {
                    ResultResponse.Error("Update account failed: user data is null")
                }
            } else {
                ResultResponse.Error(
                    "Update account failed: ${
                        response.errorBody()?.string() ?: "Unknown error"
                    }"
                )
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "updateAccount: ${e.message}", e)
            ResultResponse.Error(e.message.toString())
        }
    }

        suspend fun isSessionValid(): Boolean {
            val token = getToken() ?: return false
            return try {
                val response = authService.logout("Bearer $token")
                response.isSuccessful
            } catch (e: Exception) {
                Log.e("AuthRepository", "isSessionValid: ${e.message}", e)
                false
            }
        }

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun getToken(): String? {
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