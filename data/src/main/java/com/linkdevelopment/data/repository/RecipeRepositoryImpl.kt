package com.linkdevelopment.data.repository

import com.linkdevelopment.data.mapper.toMeal
import com.linkdevelopment.data.remote.api.MealApiService
import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val mealApiService: MealApiService
) : RecipeRepository {

    override suspend fun getRecipesByCategory(category: String): List<Meal> {
        return mealApiService
            .filterMealsByCategory(category)
            .meals
            .orEmpty()
            .map { it.toMeal() }
    }

    override suspend fun searchRecipes(query: String): List<Meal> {
        return mealApiService.searchMeals(query)
            .meals
            ?.map { it.toMeal() }
            ?: emptyList()
    }

    override suspend fun getCategories(): List<String> {
        return mealApiService.getCategories()
            .categories
            ?.mapNotNull { it.name }
            ?: emptyList()
    }
}