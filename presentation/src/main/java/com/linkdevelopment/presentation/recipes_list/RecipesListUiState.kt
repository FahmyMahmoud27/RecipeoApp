package com.linkdevelopment.presentation.recipes_list

import com.linkdevelopment.domain.model.Meal



data class RecipesListUiState(
    val searchQuery: String = "",
    val favoriteMealIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val recipes: List<Meal> = emptyList(),
    val error: String? = null,
    val selectedTab: Int = 0,
    val selectedCategory: String = "All",
    val categories: List<String> = emptyList()
)


