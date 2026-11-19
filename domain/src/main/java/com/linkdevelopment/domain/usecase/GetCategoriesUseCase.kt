package com.linkdevelopment.domain.usecase

import com.linkdevelopment.domain.repository.RecipeRepository
import javax.inject.Inject



class GetCategoriesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(): List<String> {
        return repository.getCategories()
    }
}


