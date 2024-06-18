package com.learning.pestifyapp.data.retrofit.service

import com.learning.pestifyapp.data.model.remote.PlantResponse
import com.learning.pestifyapp.data.model.remote.PlantResponseItem
import retrofit2.http.GET

interface PlantService {
    @GET("plants")
    suspend fun getAllPlants(): List<PlantResponseItem>
}