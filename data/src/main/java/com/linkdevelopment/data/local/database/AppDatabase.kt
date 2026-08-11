package com.linkdevelopment.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.linkdevelopment.data.local.dao.CachedMealsDao
import com.linkdevelopment.data.local.dao.FavoriteMealsDao
import com.linkdevelopment.data.local.entity.CachedMealEntity
import com.linkdevelopment.data.local.entity.FavoriteMealEntity


@Database(
    entities = [
        FavoriteMealEntity::class,
        CachedMealEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteMealsDao(): FavoriteMealsDao
    abstract fun cachedMealsDao(): CachedMealsDao
}


