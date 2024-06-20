package com.learning.pestifyapp.data.retrofit.service

import com.learning.pestifyapp.data.response.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AccountService {
    @GET("profile")
    suspend fun fetchUserData(@Header("Authorization") token: String): Response<ProfileResponse>
}