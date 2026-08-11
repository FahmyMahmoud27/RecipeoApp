package com.linkdevelopment.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.linkdevelopment.presentation.navigation.AppRoute

@Composable
fun BottomBar(
    navController: NavController,
    currentRoute: String?
) {
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == AppRoute.Home.route,
            colors = itemColors,
            onClick = {
                navController.navigate(AppRoute.Home.route) {
                    launchSingleTop = true
                    popUpTo(AppRoute.Home.route) {
                        saveState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text("Home")
            }
        )
        NavigationBarItem(
            selected = currentRoute == AppRoute.Favorites.route,
            colors = itemColors,
            onClick = {
                navController.navigate(AppRoute.Favorites.route) {
                    launchSingleTop = true
                    popUpTo(AppRoute.Home.route) {
                        saveState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites"
                )
            },
            label = {
                Text("Favorites")
            }
        )
    }
}

