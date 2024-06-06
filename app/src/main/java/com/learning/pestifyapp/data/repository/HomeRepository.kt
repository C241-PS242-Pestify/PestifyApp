package com.learning.pestifyapp.data.repository

import android.content.Context
import com.learning.pestifyapp.data.model.plant.FakePlantData
import com.learning.pestifyapp.data.model.plant.PlantData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class HomeRepository {

    private val plantList = mutableListOf<PlantData>()

    init {
        if (plantList.isEmpty()) {
            plantList.addAll(FakePlantData.dummyPlants)
        }
    }

    fun getAllPlants() : Flow<List<PlantData>> {
        return flowOf(plantList)
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeRepository? = null

        @JvmStatic
        fun getInstance(): HomeRepository =
            INSTANCE ?: synchronized(this) {
                HomeRepository().apply {
                    INSTANCE = this
                }
            }
    }
}