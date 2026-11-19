package com.linkdevelopment.presentation.recipes_list

import com.linkdevelopment.domain.model.Meal

sealed class RecipesListEvent {
    data class SearchQueryChanged(val query: String) : RecipesListEvent()
    data class CategorySelected(val category: String) : RecipesListEvent()
    data class ToggleFavorite(val meal: Meal) : RecipesListEvent()
    data class TabSelected(val tab: Int) : RecipesListEvent()
    data object Retry : RecipesListEvent()
}
