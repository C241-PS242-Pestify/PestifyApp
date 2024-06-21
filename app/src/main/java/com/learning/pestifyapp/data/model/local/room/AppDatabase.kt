package com.learning.pestifyapp.data.model.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learning.pestifyapp.data.model.local.dao.ArticleDao
import com.learning.pestifyapp.data.model.local.dao.HistoryImageDao
import com.learning.pestifyapp.data.model.local.dao.PespediaDao
import com.learning.pestifyapp.data.model.local.dao.PlantDao
import com.learning.pestifyapp.data.model.local.entity.ArticleEntity
import com.learning.pestifyapp.data.model.local.entity.HistoryImageEntity
import com.learning.pestifyapp.data.model.local.entity.PespediaEntity
import com.learning.pestifyapp.data.model.local.entity.PlantEntity

@Database(entities = [PlantEntity::class, ArticleEntity::class, PespediaEntity::class,HistoryImageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao

    abstract fun articleDao(): ArticleDao

    abstract fun pespediaDao(): PespediaDao

    abstract fun historyImageDao(): HistoryImageDao



    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "database"
                    ).build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}