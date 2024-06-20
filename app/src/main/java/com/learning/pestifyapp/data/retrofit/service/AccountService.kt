package com.learning.pestifyapp.data.retrofit.service

import com.learning.pestifyapp.data.model.remote.AccountUpdateResponse
import com.learning.pestifyapp.data.model.remote.ProfileResponse
import com.learning.pestifyapp.data.model.remote.UpdateImageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AccountService {
    @PUT("auth/update")
    suspend fun updateAccount(
        @Body request: UpdateAccountRequest,
    ): Response<AccountUpdateResponse>
    @GET("pages/profile")
    suspend fun fetchUserData(): Response<ProfileResponse>
    @PUT("update-photo")
    suspend fun uploadProfilePhoto(@Body photoUrlRequest: PhotoUrlRequest): Response<UpdateImageResponse>
}