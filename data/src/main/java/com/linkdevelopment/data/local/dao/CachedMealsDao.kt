package com.linkdevelopment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linkdevelopment.data.local.entity.CachedMealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedMealsDao {

    @Query("SELECT * FROM cached_meals WHERE category = :category")
    suspend fun getMealsByCategory(category: String): List<CachedMealEntity>

    @Query("SELECT * FROM cached_meals")
    suspend fun getAllMeals(): List<CachedMealEntity>

    @Query("SELECT DISTINCT category FROM cached_meals WHERE category != ''")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT * FROM cached_meals WHERE name LIKE '%' || :query || '%'")
    suspend fun searchMeals(query: String): List<CachedMealEntity>

    @Query("SELECT * FROM cached_meals WHERE id = :id")
    suspend fun getMealById(id: String): CachedMealEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<CachedMealEntity>)

    @Query("SELECT * FROM cached_meals WHERE id IN (:ids)")
    suspend fun getMealsByIds(ids: List<String>): List<CachedMealEntity>

    @Query("DELETE FROM cached_meals WHERE category = :category")
    suspend fun deleteMealsByCategory(category: String)

    @Query("DELETE FROM cached_meals")
    suspend fun clearCache()
}
