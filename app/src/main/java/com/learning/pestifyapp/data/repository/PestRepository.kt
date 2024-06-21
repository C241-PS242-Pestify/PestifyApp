package com.learning.pestifyapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.learning.pestifyapp.data.model.pestdata.PestData
import com.learning.pestifyapp.data.model.remote.PestResponse
import com.learning.pestifyapp.data.model.remote.SavePredictionResponse
import com.learning.pestifyapp.data.retrofit.api.ApiConfig
import com.learning.pestifyapp.data.retrofit.service.PestService
import okhttp3.MultipartBody
import retrofit2.Response

class PestRepository(context: Context) {
    private val gson = Gson()
    private val pestService: PestService = ApiConfig.getPestService(context)
    private val pestSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_PEST, Context.MODE_PRIVATE)
    }

    suspend fun predictPest(image: MultipartBody.Part): Response<PestResponse> {
        val response: Response<PestResponse> =
            pestService.predictPest(image)
        if (response.isSuccessful) {
            response.body()
            return response
        } else {
            throw Exception("Failed to fetch history: ${response.message()}")
        }
    }

    suspend fun savePrediction(predictionId: String): SavePredictionResponse {
        val requestBody = PestService.SavePredictionRequest(predictionId, save = true)
        return pestService.savePrediction(requestBody)
    }

    suspend fun delPrediction(predictionId: String): SavePredictionResponse {
        val requestBody = PestService.SavePredictionRequest(predictionId, save = false)
        return pestService.savePrediction(requestBody)
    }

    fun savePestData(pestData: PestData) {
        val editor = pestSharedPreferences.edit()
        val pestDataJson = gson.toJson(pestData)
        editor.putString(KEY_PEST_DATA, pestDataJson)
        editor.apply()
    }

    fun getPestData(): PestData? {
        val pestDataJson = pestSharedPreferences.getString(KEY_PEST_DATA, null)
        return pestDataJson?.let {
            gson.fromJson(it, PestData::class.java)
        }
    }

    companion object {
        private const val PREF_PEST = "PestDataPrefs"
        private const val KEY_PEST_DATA = "key_pest_data"

        @Volatile
        private var INSTANCE: PestRepository? = null

        @JvmStatic
        fun getInstance(context: Context): PestRepository =
            INSTANCE ?: synchronized(this) {
                PestRepository(context).apply {
                    INSTANCE = this
                }
            }
    }
}