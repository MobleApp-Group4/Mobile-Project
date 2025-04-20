package com.example.todo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo.ui.components.FilterChip
import com.example.todo.ui.components.RecipeList
import com.example.todo.viewmodel.RecipesViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    recipesViewModel: RecipesViewModel
) {
    val categories = listOf("Vegan","Vegetarian","Pescatarian","Primal","Whole30","DairyFree","Paleo","GlutenFree")
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var searchWord by remember { mutableStateOf("") }

    // Fetch movies if not already fetched
    LaunchedEffect(Unit) {
        recipesViewModel.fetchAllRecipes()
    }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory != null) {
            recipesViewModel.fetchRecipesByDiet(selectedCategory)
        }
    }


    val filteredRecipes = recipesViewModel.allRecipes.filter {
        it.title.contains(searchWord, ignoreCase = true)
    }


    // Layout
    Column(modifier = modifier.padding(16.dp)) {

        OutlinedTextField(
            value = searchWord,
            onValueChange = { searchWord = it },
            placeholder = { Text("Search Recipe") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            categories.forEach { category ->
                FilterChip(
                    category = category,
                    selected = selectedCategory == category, // Check if the category is selected
                    onClick = {
                        selectedCategory = if (selectedCategory == category) null.toString() else category // Toggle category selection
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
        RecipeList(recipes=filteredRecipes,navController = navController)

    }
}