package com.learning.pestifyapp.data.model.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "plant")
data class PlantEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "imageUrl")
    val picture: String
)