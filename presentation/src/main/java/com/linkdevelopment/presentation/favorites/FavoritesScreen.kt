package com.linkdevelopment.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linkdevelopment.presentation.components.RecipeCard

@Composable
fun FavoritesScreen(
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Something went wrong",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.favorites.isEmpty() -> {
                Text(
                    text = "No favorite recipes yet",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp
                    ),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.favorites,
                        key = { meal -> meal.id }
                    ) { meal ->

                        RecipeCard(
                            meal = meal,
                            isFavorite = true,
                            onFavoriteClick = {
                                viewModel.removeFavorite(meal)
                            },
                            onCardClick = {
                                onMealClick(meal.id)
                            }
                        )
                    }
                }
            }
        }
    }
}



