package com.linkdevelopment.data.repository

import com.linkdevelopment.data.local.entity.CachedMealEntity
import com.linkdevelopment.data.local.entity.FavoriteMealEntity
import com.linkdevelopment.data.local.entity.FavoriteMealWithCached
import com.linkdevelopment.data.local.localdatasource.CachedMealsLocalDataSource
import com.linkdevelopment.data.local.localdatasource.FavoriteMealsLocalDataSource
import com.linkdevelopment.data.mapper.toCachedEntity
import com.linkdevelopment.data.mapper.toMeal
import com.linkdevelopment.data.remote.api.MealApiService
import com.linkdevelopment.data.remote.dto.MealDto
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
        val isAll = category == "All"
        return executeWithCacheFallback(
            networkAction = {
                val response = if (isAll) mealApiService.searchMeals("") else mealApiService.filterMealsByCategory(category)
                val networkMeals = response.meals.orEmpty()
                val merged = mergeWithCache(networkMeals, if (isAll) null else category)
                if (!isAll) cachedMealsLocalDataSource.deleteMealsByCategory(category)
                cachedMealsLocalDataSource.saveMeals(merged)
                merged.map { it.toMeal() }
            },
            localAction = {
                val cached = if (isAll) cachedMealsLocalDataSource.getAllMeals() else cachedMealsLocalDataSource.getMealsByCategory(category)
                cached.map { it.toMeal() }
            },
            isCacheEmpty = { it.isNullOrEmpty() }
        )
    }

    override suspend fun searchRecipes(query: String): List<Meal> {
        return executeWithCacheFallback(
            networkAction = {
                val response = mealApiService.searchMeals(query)
                val merged = mergeWithCache(response.meals.orEmpty(), null)
                cachedMealsLocalDataSource.saveMeals(merged)
                merged.map { it.toMeal() }
            },
            localAction = { cachedMealsLocalDataSource.searchMeals(query).map { it.toMeal() } },
            isCacheEmpty = { it.isNullOrEmpty() }
        )
    }

    override suspend fun getCategories(): List<String> {
        return executeWithCacheFallback(
            networkAction = {
                mealApiService.getCategories().categories?.mapNotNull { it.name } ?: emptyList()
            },
            localAction = { cachedMealsLocalDataSource.getAllCategories() },
            isCacheEmpty = { it.isNullOrEmpty() }
        )
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
        return executeWithCacheFallback(
            networkAction = {
                val response = mealApiService.getMealDetails(mealId)
                val mealDto = response.meals?.firstOrNull() ?: throw IllegalStateException("Meal not found")
                cachedMealsLocalDataSource.saveMeals(listOf(mealDto.toCachedEntity()))
                mealDto.toMeal()
            },
            localAction = { cachedMealsLocalDataSource.getMealById(mealId)?.toMeal() },
            isCacheEmpty = { it == null }
        )
    }

    private suspend fun <T> executeWithCacheFallback(
        networkAction: suspend () -> T,
        localAction: suspend () -> T?,
        isCacheEmpty: (T?) -> Boolean
    ): T {
        if (!checkNetworkState.isConnected()) {
            val cache = localAction()
            if (isCacheEmpty(cache)) throw AppError.NoCacheAvailable
            return cache!!
        }

        return try {
            networkAction()
        } catch (e: Exception) {
            if (e is kotlinx.coroutines.CancellationException) throw e
            val cache = localAction()
            if (isCacheEmpty(cache)) throw mapToAppError(e, true)
            cache!!
        }
    }

    private suspend fun mergeWithCache(networkMeals: List<MealDto>, category: String?): List<CachedMealEntity> {
        val existingCached = cachedMealsLocalDataSource.getMealsByIds(networkMeals.mapNotNull { it.id })
        return networkMeals.map { dto ->
            val cached = existingCached.find { it.id == dto.id }
            dto.toCachedEntity(category).copy(
                instructions = if (dto.instructions.isNullOrBlank()) cached?.instructions.orEmpty() else dto.instructions.orEmpty(),
                area = if (dto.area.isNullOrBlank()) cached?.area.orEmpty() else dto.area.orEmpty(),
                youtubeUrl = if (dto.youtubeUrl.isNullOrBlank()) cached?.youtubeUrl.orEmpty() else dto.youtubeUrl.orEmpty()
            )
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
