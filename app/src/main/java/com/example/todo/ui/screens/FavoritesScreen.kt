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
import com.example.todo.ui.components.RecipeList
import com.example.todo.viewmodel.RecipesViewModel
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavoriteScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    recipesViewModel: RecipesViewModel,
    ) {
    var searchWord by remember { mutableStateOf("") }
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    LaunchedEffect(userId) {
        if (userId != null){
            userViewModel.getFavoriteRecipes(userId)
        }
    }
    val favoriteRecipes = userViewModel.favoriteRecipes
    val filteredRecipes = favoriteRecipes.filter{
        it.title.contains(searchWord, ignoreCase = true)
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {

        OutlinedTextField(
            value = searchWord,
            onValueChange = { searchWord = it },
            placeholder = { Text("Search Recipe") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            if (filteredRecipes.isEmpty()) {
                Text(text = "No favorite recipes found.", modifier = modifier.padding(16.dp))
            } else {
                RecipeList(recipes = filteredRecipes, navController = navController)
            }
        }
    }
}
