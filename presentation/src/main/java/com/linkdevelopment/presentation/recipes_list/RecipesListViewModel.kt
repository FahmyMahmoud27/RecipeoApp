package com.linkdevelopment.presentation.recipes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdevelopment.domain.model.AppError
import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.domain.usecase.AddFavoriteUseCase
import com.linkdevelopment.domain.usecase.GetCategoriesUseCase
import com.linkdevelopment.domain.usecase.GetFavoriteMealsUseCase
import com.linkdevelopment.domain.usecase.GetRecipesByCategoryUseCase
import com.linkdevelopment.domain.usecase.RemoveFavoriteUseCase
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
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val getFavoriteMealsUseCase: GetFavoriteMealsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesListUiState())
    val uiState: StateFlow<RecipesListUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null
    private var dataJob: Job? = null

    init {
        _uiState.update { it.copy(selectedCategory = "All") }
        fetchData()
        observeFavorites()
    }

    fun onEvent(event: RecipesListEvent) {
        when (event) {
            is RecipesListEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                searchRecipes(event.query)
            }
            is RecipesListEvent.CategorySelected -> {
                _uiState.update { 
                    it.copy(
                        selectedCategory = event.category,
                        searchQuery = ""
                    ) 
                }
                fetchData()
            }
            is RecipesListEvent.ToggleFavorite -> toggleFavorite(event.meal)
            is RecipesListEvent.TabSelected -> selectTab(event.tab)
            RecipesListEvent.Retry -> fetchData()
        }
    }

    private fun fetchData() {
        searchJob?.cancel()
        dataJob?.cancel()
        dataJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Load categories if missing
                if (uiState.value.categories.isEmpty()) {
                    try {
                        val categories = getCategoriesUseCase()
                        _uiState.update { it.copy(categories = categories) }
                    } catch (e: Exception) {
                        // Non-fatal if we already have some state or if getRecipes succeeds
                        if (e is kotlinx.coroutines.CancellationException) throw e
                    }
                }

                val currentCategory = uiState.value.selectedCategory
                val recipes = getRecipesByCategoryUseCase(currentCategory)

                _uiState.update {
                    it.copy(
                        recipes = recipes,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        if (e is kotlinx.coroutines.CancellationException) return
        
        val errorMessage = if (e is AppError) {
            e.getUserFriendlyMessage()
        } else {
            "Something went wrong. Please try again."
        }
        _uiState.update {
            it.copy(
                isLoading = false,
                error = errorMessage
            )
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoriteMealsUseCase().collect { favorites ->
                _uiState.update {
                    it.copy(
                        favoriteMealIds = favorites.map { meal -> meal.id }.toSet()
                    )
                }
            }
        }
    }

    private fun toggleFavorite(meal: Meal) {
        viewModelScope.launch {
            if (meal.id in uiState.value.favoriteMealIds) {
                removeFavoriteUseCase(meal)
            } else {
                addFavoriteUseCase(meal)
            }
        }
    }

    private fun selectTab(tab: Int) {
        _uiState.update {
            it.copy(
                selectedTab = tab
            )
        }
    }

    private fun searchRecipes(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500.milliseconds)
            if (query.isBlank()) {
                fetchData()
                return@launch
            }
            
            dataJob?.cancel()
            _uiState.update { it.copy(isLoading = true, error = null) }
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
                handleError(e)
            }
        }
    }
}
