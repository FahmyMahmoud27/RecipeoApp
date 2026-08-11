package com.linkdevelopment.presentation.navigation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.linkdevelopment.presentation.components.BottomBar
import com.linkdevelopment.presentation.details.MealDetailsScreen
import com.linkdevelopment.presentation.favorites.FavoritesScreen
import com.linkdevelopment.presentation.recipes_list.RecipesListScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val context = LocalContext.current

    val showBottomBar = currentRoute != AppRoute.Details.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
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
                        navController.navigate(
                            AppRoute.Details.createRoute(mealId)
                        )
                    }
                )
            }

            composable(AppRoute.Favorites.route) {
                FavoritesScreen(
                    onMealClick = { mealId ->
                        navController.navigate(
                            AppRoute.Details.createRoute(mealId)
                        )
                    }
                )
            }

            composable(
                route = AppRoute.Details.route,
                arguments = listOf(
                    navArgument("mealId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val mealId = backStackEntry.arguments?.getString("mealId")

                if (mealId != null) {
                    MealDetailsScreen(
                        mealId = mealId,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onYouTubeClick = { url ->
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                url.toUri()
                            )
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

sealed class AppRoute(
    val route: String
) {
    data object Home : AppRoute("home")
    data object Favorites : AppRoute("favorites")

    data object Details : AppRoute("details/{mealId}") {

        fun createRoute(mealId: String): String {
            return "details/$mealId"
        }
    }

}