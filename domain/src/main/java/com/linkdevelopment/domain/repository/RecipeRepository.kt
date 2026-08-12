package com.linkdevelopment.domain.repository

import com.linkdevelopment.domain.model.Meal

interface RecipeRepository {

    suspend fun getRecipesByCategory(category: String): List<Meal>

}