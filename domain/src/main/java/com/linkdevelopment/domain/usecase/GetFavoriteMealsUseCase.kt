package com.linkdevelopment.domain.usecase

import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMealsUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Meal>> {
        return recipeRepository.getFavorites()
    }
}

