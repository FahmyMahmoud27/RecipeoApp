package com.linkdevelopment.data.repository

import com.linkdevelopment.data.local.entity.FavoriteMealEntity
import com.linkdevelopment.data.local.localdatasource.FavoriteMealsLocalDataSource
import com.linkdevelopment.data.mapper.toMeal
import com.linkdevelopment.data.remote.api.MealApiService
import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val mealApiService: MealApiService,
    private val favoriteMealsLocalDataSource: FavoriteMealsLocalDataSource
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

    private fun Meal.toFavoriteEntity(): FavoriteMealEntity {
        return FavoriteMealEntity(
            id = id,
            name = name,
            imageUrl = imageUrl,
            category = category,
            area = area
        )
    }

    override suspend fun addFavorite(meal: Meal) {
        favoriteMealsLocalDataSource.addFavorite(
            meal.toFavoriteEntity()
        )
    }

    override suspend fun removeFavorite(meal: Meal) {
        favoriteMealsLocalDataSource.removeFavorite(
            meal.toFavoriteEntity()
        )
    }

    override fun getFavorites(): Flow<List<Meal>> {
        return favoriteMealsLocalDataSource
            .getFavorites()
            .map { meals ->
                meals.map { entity ->
                    Meal(
                        id = entity.id,
                        name = entity.name,
                        imageUrl = entity.imageUrl,
                        category = entity.category,
                        area = entity.area,
                        instructions = "",
                        youtubeUrl = ""
                    )
                }
            }
    }


    override suspend fun getMealDetails(mealId: String): Meal {
        return mealApiService
            .getMealDetails(mealId)
            .meals
            ?.firstOrNull()
            ?.toMeal()
            ?: throw IllegalStateException("Meal not found")
    }


}