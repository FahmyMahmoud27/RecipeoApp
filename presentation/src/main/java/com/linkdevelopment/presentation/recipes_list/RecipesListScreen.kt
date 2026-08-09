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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linkdevelopment.domain.model.Meal
import com.linkdevelopment.presentation.components.EmptyRecipesState
import com.linkdevelopment.presentation.components.RecipeCard
import com.linkdevelopment.presentation.components.RecipeoTextField
import androidx.compose.foundation.lazy.grid.items as gridItems

@Composable
fun RecipesListScreen(
    viewModel: RecipesListViewModel = hiltViewModel(),
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = uiState.selectedTab == 0,
                    onClick = {
                        viewModel.selectTab(0)
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                    },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == 1,
                    onClick = {
                        viewModel.selectTab(1)
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites")
                    },
                    label = {
                        Text("Favorites")
                    }
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Search
            RecipeoTextField(
                value = uiState.searchQuery,
                onValueChange = {
                    viewModel.searchRecipes(it)
                },
                hint = "Search recipes",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Categories
            Text(
                text = "Category",
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
                            viewModel.selectCategory("All")
                        },
                        label = {
                            Text(text = "All")
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
                            viewModel.selectCategory(category)
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

                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "Something went wrong"
                    )
                }

                uiState.recipes.isEmpty() -> {
                    EmptyRecipesState(
                        modifier = Modifier.weight(1f)
                    )
                }
                else -> {
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
                                    viewModel.toggleFavorite(meal)
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
}


