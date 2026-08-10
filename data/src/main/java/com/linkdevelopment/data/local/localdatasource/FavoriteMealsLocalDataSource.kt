package com.linkdevelopment.data.local.localdatasource

import com.linkdevelopment.data.local.dao.FavoriteMealsDao
import com.linkdevelopment.data.local.entity.FavoriteMealEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FavoriteMealsLocalDataSource @Inject constructor(
    private val favoriteMealsDao: FavoriteMealsDao
) {

    suspend fun addFavorite(meal: FavoriteMealEntity) {
        favoriteMealsDao.insertFavorite(meal)
    }

    suspend fun removeFavorite(meal: FavoriteMealEntity) {
        favoriteMealsDao.deleteFavorite(meal)
    }

    fun getFavorites(): Flow<List<FavoriteMealEntity>> {
        return favoriteMealsDao.getFavorites()
    }
}


