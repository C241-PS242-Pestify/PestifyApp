package com.learning.pestifyapp.data.retrofit.service

import com.learning.pestifyapp.data.model.remote.ArticleResponseItem
import com.learning.pestifyapp.data.model.remote.PlantResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleService {
    @GET("articles")
    suspend fun getAllArticles(): List<ArticleResponseItem>

    @GET("{tags}/articles")
    suspend fun getArticlesByTag(tags: String): List<ArticleResponseItem>

    @GET("articles/{id}")
    suspend fun getArticleById(@Path("id") id : String): ArticleResponseItem

}