package com.learning.pestifyapp.data.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learning.pestifyapp.data.model.local.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(article: List<ArticleEntity>)

    @Query("SELECT * FROM article")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Query("SELECT * FROM article WHERE id = :id")
    suspend fun getArticleById(id: String): ArticleEntity

    @Query("DELETE FROM article")
    suspend fun deleteAllArticles()

    @Query("SELECT * FROM article WHERE tags LIKE '%' || :tag || '%'")
    suspend fun getArticlesByTag(tag: String): List<ArticleEntity>

}