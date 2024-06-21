package com.learning.pestifyapp.data.retrofit.service

import com.learning.pestifyapp.data.model.remote.LoginResponse
import com.learning.pestifyapp.data.model.remote.LogoutResponse
import com.learning.pestifyapp.data.model.remote.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

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
    suspend fun logout(): Response<LogoutResponse>



}


