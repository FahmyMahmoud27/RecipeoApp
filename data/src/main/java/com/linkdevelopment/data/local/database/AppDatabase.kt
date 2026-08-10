package com.linkdevelopment.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.linkdevelopment.data.local.dao.FavoriteMealsDao
import com.linkdevelopment.data.local.entity.FavoriteMealEntity


@Database(
    entities = [FavoriteMealEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteMealsDao(): FavoriteMealsDao
}


