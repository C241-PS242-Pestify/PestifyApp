package com.learning.pestifyapp.data.repository

import android.content.Context
import com.learning.pestifyapp.data.model.HistoryData
import com.learning.pestifyapp.data.model.local.dao.HistoryImageDao
import com.learning.pestifyapp.data.model.local.dao.PespediaDao
import com.learning.pestifyapp.data.model.local.entity.HistoryImageEntity
import com.learning.pestifyapp.data.model.remote.HistoryResponse
import com.learning.pestifyapp.data.model.remote.ListHistoryResponse
import com.learning.pestifyapp.data.retrofit.api.ApiConfig
import com.learning.pestifyapp.data.retrofit.service.HistoryService
import com.learning.pestifyapp.data.retrofit.service.PespediaService
import retrofit2.Response

class HistoryRepository(
    private val historyImageDao: HistoryImageDao,
    private val historyService: HistoryService
) {
    suspend fun saveHistoryImage(historyId: String, image: String) {
        val historyImage = HistoryImageEntity(historyId, image)
        historyImageDao.insert(historyImage)
    }

    suspend fun getHistoryImage(historyId: String): HistoryImageEntity {
        return historyImageDao.getHistoryImage(historyId)
    }
    suspend fun deleteHistoryImage(historyId: String) {
        historyImageDao.deleteHistoryImage(historyId)
    }

    suspend fun fetchHistory(): List<HistoryData> {
        val response: Response<ListHistoryResponse> =
            historyService.fetchHistory()
        if (response.isSuccessful) {

            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Failed to fetch history: ${response.message()}")
        }
    }

    suspend fun getHistoryById(historyId: String): Response<HistoryResponse> {
        val response: Response<HistoryResponse> =
            historyService.getHistoryById(historyId)
        if (response.isSuccessful) {
            response.body()?.data?.pest?.additionalImage
            return response
        } else {
            throw Exception("Failed to fetch history: ${response.message()}")
        }
    }


    suspend fun deleteHistoryById(historyId: String): Response<HistoryResponse> {
        val response: Response<HistoryResponse> =
            historyService.deleteHistoryById(historyId)

        if (response.isSuccessful) {
            return response
        } else {
            throw Exception("Failed to fetch history: ${response.message()}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HistoryRepository? = null

        @JvmStatic
        fun getInstance(
            historyImageDao: HistoryImageDao,
            apiService: HistoryService
        ): HistoryRepository =
            INSTANCE ?: synchronized(this) {
                HistoryRepository(
                    historyImageDao,
                    apiService
                ).apply {
                    INSTANCE = this
                }
            }
    }

}