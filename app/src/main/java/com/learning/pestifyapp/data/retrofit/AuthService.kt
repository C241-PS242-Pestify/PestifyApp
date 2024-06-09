package com.learning.pestifyapp.data.retrofit

import com.learning.pestifyapp.data.response.AccountUpdateResponse
import com.learning.pestifyapp.data.response.LoginResponse
import com.learning.pestifyapp.data.response.LogoutResponse
import com.learning.pestifyapp.data.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class UpdateAccountRequest(
    val username: String?,
    val email: String?,
    val password: String?,
)

interface AuthService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): LoginResponse

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<LogoutResponse>

    @PUT("auth/update")
    suspend fun updateAccount(
        @Header("Authorization") token: String,
        @Body request: UpdateAccountRequest,
    ): Response<AccountUpdateResponse>

}


