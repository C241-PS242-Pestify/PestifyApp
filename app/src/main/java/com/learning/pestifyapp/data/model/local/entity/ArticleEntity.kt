package com.learning.pestifyapp.data.model.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "tags")
    val tags: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "imageUrl")
    val picture: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: String
)