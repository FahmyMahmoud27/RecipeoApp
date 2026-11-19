package com.linkdevelopment.presentation.recipes_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linkdevelopment.presentation.R
import com.linkdevelopment.presentation.components.EmptyRecipesState
import com.linkdevelopment.presentation.components.RecipeCard
import com.linkdevelopment.presentation.components.RecipeoTextField
import androidx.compose.foundation.lazy.grid.items as gridItems

@Composable
fun RecipesListScreen(
    viewModel: RecipesListViewModel = hiltViewModel(),
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allLabel = stringResource(R.string.category_all)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Search
        RecipeoTextField(
            value = uiState.searchQuery,
            onValueChange = {
                viewModel.onEvent(RecipesListEvent.SearchQueryChanged(it))
            },
            hint = stringResource(R.string.hint_search_recipes),
            leadingIcon = Icons.Default.Search,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Categories
        Text(
            text = stringResource(R.string.label_category),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // All
            item {
                FilterChip(
                    selected = uiState.selectedCategory == "All",
                    onClick = {
                        viewModel.onEvent(RecipesListEvent.CategorySelected("All"))
                    },
                    label = {
                        Text(text = allLabel)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            // Categories
            items(uiState.categories) { category ->
                FilterChip(
                    selected = category == uiState.selectedCategory,
                    onClick = {
                        viewModel.onEvent(RecipesListEvent.CategorySelected(category))
                    },
                    label = {
                        Text(text = category)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recipes Grid
        when {

            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.recipes.isNotEmpty() -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    gridItems(
                        items = uiState.recipes,
                        key = { meal -> meal.id }
                    ) { meal ->

                        RecipeCard(
                            meal = meal,
                            isFavorite = meal.id in uiState.favoriteMealIds,
                            onFavoriteClick = {
                                viewModel.onEvent(RecipesListEvent.ToggleFavorite(meal))
                            },
                            onCardClick = {
                                onMealClick(meal.id)
                            }
                        )
                    }
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.error ?: stringResource(R.string.error_something_went_wrong)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        com.linkdevelopment.presentation.components.AppButton(
                            text = stringResource(R.string.button_retry),
                            onClick = {
                                viewModel.onEvent(RecipesListEvent.Retry)
                            }
                        )
                    }
                }
            }

            uiState.recipes.isEmpty() -> {
                EmptyRecipesState(
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}