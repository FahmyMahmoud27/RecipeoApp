package com.linkdevelopment.data.remote.api

import com.linkdevelopment.data.remote.dto.CategoryResponseDto
import com.linkdevelopment.data.remote.dto.MealResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): MealResponseDto

    @GET("lookup.php")
    suspend fun getMealDetails(
        @Query("i") mealId: String
    ): MealResponseDto

    @GET("filter.php")
    suspend fun filterMealsByCategory(
        @Query("c") category: String
    ): MealResponseDto

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponseDto
}

