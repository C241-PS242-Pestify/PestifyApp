package com.learning.pestifyapp.data.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learning.pestifyapp.data.model.local.entity.PlantEntity

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: PlantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<PlantEntity>)

    @Query("SELECT * FROM plant")
    suspend fun getAllPlants(): List<PlantEntity>

    @Query("SELECT * FROM plant WHERE id = :id")
    suspend fun getPlantById(id: String): PlantEntity

    @Query("DELETE FROM plant")
    suspend fun deleteAllPlants()
}