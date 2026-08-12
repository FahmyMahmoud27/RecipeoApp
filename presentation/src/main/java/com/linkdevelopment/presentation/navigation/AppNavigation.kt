package com.linkdevelopment.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.linkdevelopment.presentation.components.BottomBar
import com.linkdevelopment.presentation.favorites.FavoritesScreen
import com.linkdevelopment.presentation.recipes_list.RecipesListScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = AppRoute.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            composable(AppRoute.Home.route) {
                RecipesListScreen(
                    onMealClick = { mealId ->
                        // Details later
                    }
                )
            }

            composable(AppRoute.Favorites.route) {
                FavoritesScreen(
                    onMealClick = { mealId ->
                        // Details later
                    }
                )
            }
        }
    }
}

sealed class AppRoute(
    val route: String
) {
    data object Home : AppRoute("home")
    data object Favorites : AppRoute("favorites")
    data object Details : AppRoute("details")
}