package com.linkdevelopment.domain.repository

import com.linkdevelopment.domain.model.Meal

interface RecipeRepository {

    suspend fun getRecipesByCategory(category: String): List<Meal>

    suspend fun searchRecipes(query: String): List<Meal>

    suspend fun getCategories(): List<String>


}