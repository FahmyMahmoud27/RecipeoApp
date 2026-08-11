package com.linkdevelopment.data.repository

import com.linkdevelopment.data.local.entity.FavoriteMealEntity
import com.linkdevelopment.data.local.entity.FavoriteMealWithCached
import com.linkdevelopment.data.local.localdatasource.CachedMealsLocalDataSource
import com.linkdevelopment.data.local.localdatasource.FavoriteMealsLocalDataSource
import com.linkdevelopment.data.mapper.toCachedEntity
import com.linkdevelopment.data.mapper.toMeal
import com.linkdevelopment.data.remote.api.MealApiService
import com.linkdevelopment.domain.model.AppError
import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.repository.RecipeRepository
import com.linkdevelopment.domain.util.ICheckNetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val mealApiService: MealApiService,
    private val favoriteMealsLocalDataSource: FavoriteMealsLocalDataSource,
    private val cachedMealsLocalDataSource: CachedMealsLocalDataSource,
    private val checkNetworkState: ICheckNetworkState
) : RecipeRepository {

    override suspend fun getRecipesByCategory(category: String): List<Meal> {
        if (!checkNetworkState.isConnected()) {
            val cachedMeals = cachedMealsLocalDataSource.getMealsByCategory(category)
            return if (cachedMeals.isNotEmpty()) {
                cachedMeals.map { it.toMeal() }
            } else {
                throw AppError.NoCacheAvailable
            }
        }

        return try {
            val response = mealApiService.filterMealsByCategory(category)
            val networkMeals = response.meals.orEmpty()
            
            val existingCached = cachedMealsLocalDataSource.getMealsByIds(networkMeals.mapNotNull { it.id })
            val mergedEntities = networkMeals.map { dto ->
                val cached = existingCached.find { it.id == dto.id }
                dto.toCachedEntity(category).copy(
                    instructions = if (dto.instructions.isNullOrBlank()) cached?.instructions.orEmpty() else dto.instructions.orEmpty(),
                    area = if (dto.area.isNullOrBlank()) cached?.area.orEmpty() else dto.area.orEmpty(),
                    youtubeUrl = if (dto.youtubeUrl.isNullOrBlank()) cached?.youtubeUrl.orEmpty() else dto.youtubeUrl.orEmpty()
                )
            }

            cachedMealsLocalDataSource.deleteMealsByCategory(category)
            cachedMealsLocalDataSource.saveMeals(mergedEntities)

            mergedEntities.map { it.toMeal() }
        } catch (e: Exception) {
            val cachedMeals = cachedMealsLocalDataSource.getMealsByCategory(category)
            if (cachedMeals.isNotEmpty()) {
                cachedMeals.map { it.toMeal() }
            } else {
                throw mapToAppError(e, true)
            }
        }
    }

    override suspend fun searchRecipes(query: String): List<Meal> {
        if (!checkNetworkState.isConnected()) {
            val cachedMeals = cachedMealsLocalDataSource.searchMeals(query)
            return if (cachedMeals.isNotEmpty()) {
                cachedMeals.map { it.toMeal() }
            } else {
                throw AppError.NoCacheAvailable
            }
        }

        return try {
            val response = mealApiService.searchMeals(query)
            val networkMeals = response.meals.orEmpty()

            // Safe Cache Update: Prevent partial data from overwriting full details
            val existingCached = cachedMealsLocalDataSource.getMealsByIds(networkMeals.mapNotNull { it.id })
            val mergedEntities = networkMeals.map { dto ->
                val cached = existingCached.find { it.id == dto.id }
                dto.toCachedEntity().copy(
                    instructions = if (dto.instructions.isNullOrBlank()) cached?.instructions.orEmpty() else dto.instructions.orEmpty(),
                    area = if (dto.area.isNullOrBlank()) cached?.area.orEmpty() else dto.area.orEmpty(),
                    youtubeUrl = if (dto.youtubeUrl.isNullOrBlank()) cached?.youtubeUrl.orEmpty() else dto.youtubeUrl.orEmpty()
                )
            }

            cachedMealsLocalDataSource.saveMeals(mergedEntities)

            mergedEntities.map { it.toMeal() }
        } catch (e: Exception) {
            val cachedMeals = cachedMealsLocalDataSource.searchMeals(query)
            if (cachedMeals.isNotEmpty()) {
                cachedMeals.map { it.toMeal() }
            } else {
                throw mapToAppError(e, true)
            }
        }
    }

    override suspend fun getCategories(): List<String> {
        if (!checkNetworkState.isConnected()) {
            val cachedCategories = cachedMealsLocalDataSource.getAllCategories()
            return if (cachedCategories.isNotEmpty()) {
                cachedCategories
            } else {
                throw AppError.NoCacheAvailable
            }
        }

        return try {
            val categories = mealApiService.getCategories()
                .categories
                ?.mapNotNull { it.name }
                ?: emptyList()
            categories
        } catch (e: Exception) {
            val cachedCategories = cachedMealsLocalDataSource.getAllCategories()
            if (cachedCategories.isNotEmpty()) {
                cachedCategories
            } else {
                throw mapToAppError(e, true)
            }
        }
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
            .getFavoritesWithCached()
            .map { list ->
                list.map { item ->
                    val entity = item.favorite
                    val cached = item.cached
                    Meal(
                        id = entity.id,
                        name = entity.name,
                        imageUrl = entity.imageUrl,
                        category = entity.category,
                        area = cached?.area ?: entity.area,
                        instructions = cached?.instructions ?: "",
                        youtubeUrl = cached?.youtubeUrl ?: ""
                    )
                }
            }
    }


    override suspend fun getMealDetails(mealId: String): Meal {
        if (!checkNetworkState.isConnected()) {
            val cachedMeal = cachedMealsLocalDataSource.getMealById(mealId)
            return cachedMeal?.toMeal() ?: throw AppError.NoCacheAvailable
        }

        return try {
            val response = mealApiService.getMealDetails(mealId)
            val mealDto = response.meals?.firstOrNull() ?: throw IllegalStateException("Meal not found")
            val meal = mealDto.toMeal()
            cachedMealsLocalDataSource.saveMeals(listOf(mealDto.toCachedEntity()))
            meal
        } catch (e: Exception) {
            val cachedMeal = cachedMealsLocalDataSource.getMealById(mealId)
            cachedMeal?.toMeal() ?: throw mapToAppError(e, true)
        }
    }

    private fun mapToAppError(e: Exception, isCacheEmpty: Boolean): Exception {
        return when (e) {
            is AppError -> e
            is SocketTimeoutException -> if (isCacheEmpty) AppError.NoCacheAvailable else AppError.Timeout
            is UnknownHostException -> if (isCacheEmpty) AppError.NoCacheAvailable else AppError.NoInternet
            is IOException -> if (isCacheEmpty) AppError.NoCacheAvailable else AppError.NoInternet
            is HttpException -> AppError.ServerError
            else -> AppError.Unknown
        }
    }
}
