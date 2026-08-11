package com.linkdevelopment.domain.repository

import com.linkdevelopment.domain.model.Meal
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipesByCategory(category: String): List<Meal>

    suspend fun searchRecipes(query: String): List<Meal>

    suspend fun getCategories(): List<String>


    suspend fun addFavorite(meal: Meal)

    suspend fun removeFavorite(meal: Meal)

    fun getFavorites(): Flow<List<Meal>>

    suspend fun getMealDetails(mealId: String): Meal


}