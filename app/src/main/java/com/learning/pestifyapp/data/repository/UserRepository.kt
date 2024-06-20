package com.learning.pestifyapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.learning.pestifyapp.data.model.remote.AccountUpdateResponse
import com.learning.pestifyapp.data.model.remote.LoginResponse
import com.learning.pestifyapp.data.model.remote.RegisterResponse
import com.learning.pestifyapp.data.model.remote.UpdateImageResponse
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.data.retrofit.api.ApiConfig
import com.learning.pestifyapp.data.retrofit.service.AccountService
import com.learning.pestifyapp.data.retrofit.service.AuthService
import com.learning.pestifyapp.data.retrofit.service.LoginRequest
import com.learning.pestifyapp.data.retrofit.service.PhotoUrlRequest
import com.learning.pestifyapp.data.retrofit.service.RegisterRequest
import com.learning.pestifyapp.data.retrofit.service.UpdateAccountRequest
import com.learning.pestifyapp.ui.common.ResultResponse
import retrofit2.Response

class UserRepository(context: Context) {
    private val authSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE) }
    private val authService: AuthService = ApiConfig.getAuthService(context)
    private val accountService: AccountService = ApiConfig.getAccountService(context)


    //AUTHENTICATION
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
            response.token?.let {
                saveToken(it)
            }
            ResultResponse.Success(response)
        } catch (e: Exception) {
            Log.e("AuthRepository", "login: ${e.message}", e)
            ResultResponse.Error(e.message.toString())
        }
    }

    suspend fun logout(): ResultResponse<String> {
        return try {
            val response = authService.logout()
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
    suspend fun isSessionValid(): Boolean {
        return try {
            val response = authService.logout()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("AuthRepository", "isSessionValid: ${e.message}", e)
            false
        }
    }

    //AUTHSHARED PREFERENCE
    fun getUserSession(): UserData? {
        if (!getLoginStatus()) {
            return null
        } else {
            val token = authSharedPreferences.getString("token", null)
            val email = authSharedPreferences.getString("email", null)
            val username = authSharedPreferences.getString("username", null)
            val profilePhoto = authSharedPreferences.getString("profilePhoto", null)
            val isLogin = authSharedPreferences.getBoolean("isLogin", false)
            return UserData(token, email, username, profilePhoto, isLogin)
        }
    }
    private fun saveUserSession(userSession: UserData) {
        val editor = authSharedPreferences.edit()
        editor.putString("token", userSession.token)
        editor.putString("email", userSession.email)
        editor.putString("username", userSession.username)
        editor.putString("profilePhoto", userSession.profilePhoto)
        editor.putBoolean("isLogin", userSession.isLogin)
        editor.apply()
    }
    fun getLoginStatus(): Boolean {
        return authSharedPreferences.getBoolean("isLogin", false)
    }
    private fun getToken(): String? {
        return authSharedPreferences.getString("token", null)
    }
    private fun clearSession() {
        val editor = authSharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    private fun saveToken(token: String) {
        val editor = authSharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    //USER
    suspend fun fetchUserData(): ResultResponse<UserData> {
        return try {
            val response = accountService.fetchUserData()
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!.user
                val userData = UserData(getToken(), user?.email, user?.username, user?.profilePhoto, true)
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
        return try {
            Log.d(
                "AuthRepository",
                "updateAccount request with name: $name, email: $email, password: $password"
            )
            val request = UpdateAccountRequest(name, email, password)
            val response: Response<AccountUpdateResponse> = accountService.updateAccount(request)
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!.updatedUser
                if (user != null) {
                    val userData =
                        UserData(getToken(), user.email, user.username, user.profilePhoto, true)
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
    fun saveProfilePhotoUri(uri: String) {
        val editor = authSharedPreferences.edit()
        editor.putString("profile_photo_uri", uri.toString())
        editor.apply()
    }
    suspend fun uploadProfilePhoto(photoUrl: String): Response<UpdateImageResponse> {
        val request = PhotoUrlRequest(photoUrl)
        return accountService.uploadProfilePhoto(request)
    }

    companion object {
        private const val PREF_AUTH = "auth_prefs"

        @Volatile
        private var INSTANCE: UserRepository? = null

        @JvmStatic
        fun getInstance(context: Context): UserRepository = INSTANCE ?: synchronized(this) {
            UserRepository(context).apply {
                INSTANCE = this
            }
        }
    }
}
