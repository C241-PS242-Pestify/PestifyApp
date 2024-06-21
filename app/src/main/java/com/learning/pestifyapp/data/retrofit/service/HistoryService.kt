package com.learning.pestifyapp.data.retrofit.service

import com.learning.pestifyapp.data.model.remote.HistoryResponse
import com.learning.pestifyapp.data.model.remote.ListHistoryResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path


interface HistoryService {
    @GET("history")
    suspend fun fetchHistory(): Response<ListHistoryResponse>

    @GET("history/{id}")
    suspend fun getHistoryById(
        @Path("id") id: String,
    ): Response<HistoryResponse>

    @DELETE("history/{id}")
    suspend fun deleteHistoryById(
        @Path("id") id: String,
    ): Response<HistoryResponse>
}