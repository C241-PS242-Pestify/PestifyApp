package com.learning.pestifyapp.data.repository

import android.content.Context
import com.learning.pestifyapp.data.model.homeart.Article
import com.learning.pestifyapp.data.model.homeart.FakeArtData
import com.learning.pestifyapp.data.model.plant.FakePlantData
import com.learning.pestifyapp.data.model.plant.PlantData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class HomeRepository {

    private val plantList = mutableListOf<PlantData>()
    private val articleList = mutableListOf<Article>()

    init {
        if (plantList.isEmpty()) {
            plantList.addAll(FakePlantData.dummyPlants)
        }

        if (articleList.isEmpty()) {
            articleList.addAll(FakeArtData.dummyArticles)
        }
    }

    fun getAllArticles(category: String) : Flow<List<Article>> {
        return if (category == "All") {
            flowOf(articleList)
        } else {
            flowOf(articleList.filter { it.category == category })
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