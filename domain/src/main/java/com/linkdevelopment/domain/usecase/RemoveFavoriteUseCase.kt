package com.linkdevelopment.domain.usecase

import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.repository.RecipeRepository
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(meal: Meal) {
        recipeRepository.removeFavorite(meal)
    }
}