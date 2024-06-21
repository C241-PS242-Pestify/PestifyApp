package com.learning.pestifyapp.data.retrofit.service

import com.learning.pestifyapp.data.model.remote.PespediaResponseItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PespediaService {
    @GET("ensiklopedia")
    suspend fun getAllPespedia(): List<PespediaResponseItem>

    @GET("ensiklopedia/{id}")
    suspend fun getPespediaById(@Path("id") id: String): PespediaResponseItem

    @GET("search/ensiklopedia")
    suspend fun searchPespedia(@Query("title") title: String): List<PespediaResponseItem>
}