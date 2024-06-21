package com.learning.pestifyapp.data.model

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    private val authSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${getToken()}")
            .build()
        return chain.proceed(newRequest)
    }

    private fun getToken(): String? {
        return authSharedPreferences.getString("token", null)
    }
}