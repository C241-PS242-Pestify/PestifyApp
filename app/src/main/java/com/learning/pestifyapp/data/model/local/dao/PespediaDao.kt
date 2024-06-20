package com.learning.pestifyapp.data.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learning.pestifyapp.data.model.local.entity.PespediaEntity

@Dao
interface PespediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pespediaList: List<PespediaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pespedia: PespediaEntity)

    @Query("SELECT * FROM pespedia")
    suspend fun getAllPespedia(): List<PespediaEntity>

    @Query("SELECT * FROM pespedia WHERE id = :id")
    suspend fun getPespediaById(id: String): PespediaEntity

    @Query("DELETE FROM pespedia")
    suspend fun deleteAllPespedia()
}