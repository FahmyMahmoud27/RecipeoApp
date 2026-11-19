package com.linkdevelopment.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdevelopment.domain.model.AppError
import com.linkdevelopment.domain.usecase.AddFavoriteUseCase
import com.linkdevelopment.domain.usecase.GetFavoriteMealsUseCase
import com.linkdevelopment.domain.usecase.GetMealDetailsUseCase
import com.linkdevelopment.domain.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(
    private val getMealDetailsUseCase: GetMealDetailsUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val getFavoriteMealsUseCase: GetFavoriteMealsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealDetailsUiState())
    val uiState: StateFlow<MealDetailsUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }
    fun getMealDetails(mealId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val meal = getMealDetailsUseCase(mealId)
                _uiState.update {
                    it.copy(
                        meal = meal,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) return@launch
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
        }
    }
    private fun observeFavorites() {
        viewModelScope.launch {
            combine(
                getFavoriteMealsUseCase(),
                _uiState.map { it.meal?.id }.distinctUntilChanged()
            ) { favorites, mealId ->
                mealId != null && favorites.any { it.id == mealId }
            }.collect { isFavorite ->
                _uiState.update {
                    it.copy(
                        isFavorite = isFavorite
                    )
                }
            }
        }
    }
    fun toggleFavorite() {
        val meal = uiState.value.meal ?: return

        viewModelScope.launch {
            if (uiState.value.isFavorite) {
                removeFavoriteUseCase(meal)
            } else {
                addFavoriteUseCase(meal)
            }
        }
    }
}