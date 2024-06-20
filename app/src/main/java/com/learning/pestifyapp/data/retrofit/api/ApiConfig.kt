package com.learning.pestifyapp.data.retrofit.api

import android.content.Context
import com.learning.pestifyapp.BuildConfig
import com.learning.pestifyapp.data.model.AuthInterceptor
import com.learning.pestifyapp.data.retrofit.service.AccountService
import com.learning.pestifyapp.data.retrofit.service.ArticleService
import com.learning.pestifyapp.data.retrofit.service.AuthService
import com.learning.pestifyapp.data.retrofit.service.HistoryService
import com.learning.pestifyapp.data.retrofit.service.PespediaService
import com.learning.pestifyapp.data.retrofit.service.PestService
import com.learning.pestifyapp.data.retrofit.service.PlantService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private fun getApiClient(context: Context): Retrofit {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val authInterceptor = AuthInterceptor(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAuthService(context: Context): AuthService {
        return getApiClient(context).create(AuthService::class.java)
    }

    fun getPageService(context: Context): AccountService {
        return getApiClient(context).create(AccountService::class.java)
    }

    fun getPlantService(context: Context): PlantService {
        return getApiClient(context).create(PlantService::class.java)
    }

    fun getArticleService(context: Context): ArticleService {
        return getApiClient(context).create(ArticleService::class.java)
    }

    fun getPespediaService(context: Context): PespediaService {
        return getApiClient(context).create(PespediaService::class.java)
    }

    fun getPestService(context: Context): PestService {
        return getApiClient(context).create(PestService::class.java)
    }

    fun getHistoryServie(context: Context): HistoryService {
        return getApiClient(context).create(HistoryService::class.java)
    }


}