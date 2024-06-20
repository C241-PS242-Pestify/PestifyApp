package com.learning.pestifyapp.data.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.learning.pestifyapp.data.model.local.entity.HistoryImageEntity

@Dao
interface HistoryImageDao {
    @Insert
    suspend fun insert(historyImage: HistoryImageEntity)

    @Query("SELECT historyId FROM history_image WHERE historyId = :historyId")
    suspend fun getHistoryId(historyId: String): String

    @Query("SELECT * FROM history_image WHERE historyId = :historyId")
    suspend fun getHistoryImage(historyId: String): HistoryImageEntity

    @Query("DELETE FROM history_image WHERE historyId = :historyId")
    suspend fun deleteHistoryImage(historyId: String)
}