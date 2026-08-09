package com.linkdevelopment.domain.usecase

import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.repository.RecipeRepository
import javax.inject.Inject



class SearchRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(query: String): List<Meal> {
        return repository.searchRecipes(query)
    }
}


