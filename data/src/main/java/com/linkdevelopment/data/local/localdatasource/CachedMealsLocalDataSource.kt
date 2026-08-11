package com.linkdevelopment.data.local.localdatasource

import com.linkdevelopment.data.local.entity.CachedMealEntity

interface CachedMealsLocalDataSource {
    suspend fun getMealsByCategory(category: String): List<CachedMealEntity>
    suspend fun getMealsByIds(ids: List<String>): List<CachedMealEntity>
    suspend fun getAllCategories(): List<String>
    suspend fun searchMeals(query: String): List<CachedMealEntity>
    suspend fun getMealById(id: String): CachedMealEntity?
    suspend fun saveMeals(meals: List<CachedMealEntity>)
    suspend fun deleteMealsByCategory(category: String)
    suspend fun clearCache()
}
