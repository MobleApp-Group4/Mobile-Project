package com.example.todo.viewmodel


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.api.RecipesApi
import com.example.todo.model.Recipe
import com.example.todo.model.RecipeDetail
import kotlinx.coroutines.launch

class RecipesViewModel:  ViewModel()  {

    var allRecipes = mutableStateListOf<Recipe>()
        private set


    var recipeDetail = mutableStateOf<RecipeDetail?>(null)
        private set

    fun fetchAllRecipes() {
        viewModelScope.launch {
            try {
                val response = RecipesApi.recipesService.getAllRecipes()
                allRecipes.clear()
                allRecipes.addAll(response.results)
            } catch (e: Exception) {
                Log.d("ERROR", "Failed to fetch recipes: ${e.message}")
            }
        }
    }

    fun fetchRecipesByDiet(diet: String?) {
        viewModelScope.launch {
            try {
                val response = RecipesApi.recipesService.getRecipesByDiet(diet = diet)
                allRecipes.clear()
                allRecipes.addAll(response.results)
                Log.d("RecipesViewModel", "Successfully fetched Recipe List")
            } catch (e: Exception) {
                Log.d("ERROR", "Failed to fetch recipes by diet: ${e.message}")            }
        }
    }

    fun fetchRecipeDetail(recipeId: Int) {
        viewModelScope.launch {
            try {
                val response = RecipesApi.recipesService.getRecipeDetail(recipeId)
                recipeDetail.value = response  // 保存详细信息
                Log.d("RecipesViewModel", "Successfully fetched Recipe Detail")
            } catch (e: Exception) {
                Log.d("ERROR", "Failed to fetch recipe details: ${e.message}")
            }
        }
    }



}