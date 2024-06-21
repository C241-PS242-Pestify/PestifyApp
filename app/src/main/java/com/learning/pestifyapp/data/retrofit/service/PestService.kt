package com.learning.pestifyapp.data.retrofit.service

import com.learning.pestifyapp.data.model.remote.PestResponse
import com.learning.pestifyapp.data.model.remote.SavePredictionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PestService {
    data class SavePredictionRequest(
        val predictionId: String,
        val save: Boolean,
    )
    @Multipart
    @POST("predict")
    suspend fun predictPest(
        @Part image: MultipartBody.Part,
    ): Response<PestResponse>

    @POST("predict/save")
    suspend fun savePrediction(
        @Body requestBody: SavePredictionRequest,
    ): SavePredictionResponse


}