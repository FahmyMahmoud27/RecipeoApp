package com.linkdevelopment.domain.usecase

import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.repository.RecipeRepository
import javax.inject.Inject


class GetMealDetailsUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    suspend operator fun invoke(mealId: String): Meal {
        return recipeRepository.getMealDetails(mealId)
    }
}


