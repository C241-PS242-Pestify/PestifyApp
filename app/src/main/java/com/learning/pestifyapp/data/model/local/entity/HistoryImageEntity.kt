package com.learning.pestifyapp.data.model.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_image")
data class HistoryImageEntity(
    @PrimaryKey val historyId: String,
    val image: String
)
