package com.linkdevelopment.presentation.details

import com.linkdevelopment.domain.model.Meal

data class MealDetailsUiState(
    val meal: Meal? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false
)

