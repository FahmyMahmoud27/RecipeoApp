package com.linkdevelopment.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.linkdevelopment.presentation.navigation.AppRoute

@Composable
fun BottomBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == AppRoute.Home.route,
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


