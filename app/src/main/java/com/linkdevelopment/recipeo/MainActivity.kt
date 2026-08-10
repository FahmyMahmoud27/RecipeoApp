package com.linkdevelopment.recipeo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.linkdevelopment.presentation.navigation.AppNavigation
import com.linkdevelopment.presentation.recipes_list.RecipesListScreen
import com.linkdevelopment.presentation.ui.theme.RecipeoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var favoriteMealIds by remember {
                mutableStateOf<Set<String>>(emptySet())
            }

            RecipeoTheme {
                AppNavigation()
            }
        }
    }
}

