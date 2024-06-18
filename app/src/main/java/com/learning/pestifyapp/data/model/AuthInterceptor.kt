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
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJiMmE4NWM0OS0xMmQ4LTQ5NzQtOTc5OC1kYjkzNDA2MTRmNGIiLCJ1c2VybmFtZSI6ImFsZmkiLCJlbWFpbCI6ImFsZmlAZ21haWwuY29tIiwiaWF0IjoxNzE4ODMxMTUyLCJleHAiOjE3MTg5MTc1NTJ9.cs9SnUAvnNNkMCyglrxBvLaHeoxk1QDZO2zYkbDi0UI")
            .build()
        return chain.proceed(newRequest)
    }

    private fun getToken(): String? {
        return authSharedPreferences.getString("token", null)
    }
}