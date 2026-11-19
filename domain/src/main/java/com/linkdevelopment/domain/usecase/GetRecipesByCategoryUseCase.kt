package com.linkdevelopment.domain.usecase

import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipesByCategoryUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    suspend operator fun invoke(category: String): List<Meal> {
        return recipeRepository.getRecipesByCategory(category)
    }
}
