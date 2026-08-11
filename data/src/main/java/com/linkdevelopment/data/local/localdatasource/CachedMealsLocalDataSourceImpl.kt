package com.linkdevelopment.data.local.localdatasource

import com.linkdevelopment.data.local.dao.CachedMealsDao
import com.linkdevelopment.data.local.entity.CachedMealEntity
import javax.inject.Inject

class CachedMealsLocalDataSourceImpl @Inject constructor(
    private val cachedMealsDao: CachedMealsDao
) : CachedMealsLocalDataSource {

    override suspend fun getMealsByCategory(category: String): List<CachedMealEntity> {
        return cachedMealsDao.getMealsByCategory(category)
    }

    override suspend fun getMealsByIds(ids: List<String>): List<CachedMealEntity> {
        return cachedMealsDao.getMealsByIds(ids)
    }

    override suspend fun getAllCategories(): List<String> {
        return cachedMealsDao.getAllCategories()
    }

    override suspend fun searchMeals(query: String): List<CachedMealEntity> {
        return cachedMealsDao.searchMeals(query)
    }

    override suspend fun getMealById(id: String): CachedMealEntity? {
        return cachedMealsDao.getMealById(id)
    }

    override suspend fun saveMeals(meals: List<CachedMealEntity>) {
        cachedMealsDao.insertMeals(meals)
    }

    override suspend fun deleteMealsByCategory(category: String) {
        cachedMealsDao.deleteMealsByCategory(category)
    }

    override suspend fun clearCache() {
        cachedMealsDao.clearCache()
    }
}
