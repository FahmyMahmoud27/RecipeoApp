package com.linkdevelopment.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.linkdevelopment.data.local.entity.FavoriteMealEntity
import com.linkdevelopment.data.local.entity.FavoriteMealWithCached
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteMealsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(meal: FavoriteMealEntity)

    @Delete
    suspend fun deleteFavorite(meal: FavoriteMealEntity)

    @Query("SELECT * FROM favorite_meals")
    fun getFavorites(): Flow<List<FavoriteMealEntity>>

    @Transaction
    @Query("SELECT * FROM favorite_meals")
    fun getFavoritesWithCached(): Flow<List<FavoriteMealWithCached>>
}



