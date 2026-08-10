package com.linkdevelopment.presentation.favorites

import com.linkdevelopment.domain.model.Meal


data class FavoritesUiState(
    val favorites: List<Meal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

