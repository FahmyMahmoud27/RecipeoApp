package com.linkdevelopment.presentation.recipes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.usecase.GetCategoriesUseCase
import com.linkdevelopment.domain.usecase.GetRecipesByCategoryUseCase
import com.linkdevelopment.domain.usecase.SearchRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds


@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val getRecipesByCategoryUseCase: GetRecipesByCategoryUseCase,
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesListUiState())
    val uiState: StateFlow<RecipesListUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        getRecipes()
        getCategories()
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

    fun searchRecipes(query: String) {

        _uiState.update {
            it.copy(searchQuery = query)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500.milliseconds)
            if (query.isBlank()) {
                getRecipes()
                return@launch
            }
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val recipes = searchRecipesUseCase(query)
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


    fun getCategories() {
        viewModelScope.launch {
            try {
                val categories = getCategoriesUseCase()
                _uiState.update {
                    it.copy(
                        categories = categories
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message
                    )
                }
            }
        }
    }

    fun selectCategory(category: String) {
        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
        if (category == "All") {
            getRecipes()
        } else {
            getRecipesByCategory(category)
        }
    }


    private fun getRecipesByCategory(category: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val recipes = getRecipesByCategoryUseCase(category)
                _uiState.update {
                    it.copy(
                        recipes = recipes,
                        isLoading = false
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


}


