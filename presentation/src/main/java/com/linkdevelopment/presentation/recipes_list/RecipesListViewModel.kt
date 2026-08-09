package com.linkdevelopment.presentation.recipes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.usecase.GetRecipesByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val getRecipesByCategoryUseCase: GetRecipesByCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesListUiState())
    val uiState: StateFlow<RecipesListUiState> = _uiState.asStateFlow()

    init {
        getRecipes()
    }

    fun getRecipes() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val recipes = getRecipesByCategoryUseCase("Chicken")
                _uiState.update {
                    it.copy(
                        recipes = recipes,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun toggleFavorite(meal: Meal) {
        val currentFavorites = uiState.value.favoriteMealIds
        val updatedFavorites =
            if (meal.id in currentFavorites) {
                currentFavorites - meal.id
            } else {
                currentFavorites + meal.id
            }
        _uiState.update {
            it.copy(
                favoriteMealIds = updatedFavorites
            )
        }
    }

    fun selectTab(tab: Int) {
        _uiState.update {
            it.copy(
                selectedTab = tab
            )
        }
    }


}


