package com.learning.pestifyapp.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private const val BASE_URL = "API URL"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }

}