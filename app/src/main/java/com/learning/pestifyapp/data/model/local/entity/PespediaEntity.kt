package com.learning.pestifyapp.data.model.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pespedia")
data class PespediaEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "picture")
    val picture: String,

    @ColumnInfo(name = "additionalDescription1")
    val additionalDescription1: String?,

    @ColumnInfo(name = "additionalDescription2")
    val additionalDescription2: String?,
)
