package com.example.todo.model


import android.media.Image
import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    val results: List<Recipe>,
    val number: Int
)

data class Recipe(
    var id: Int,
    var title: String,
    var image: String,

)

data class RecipeDetail(
    var id: Int,
    var title: String,
    var image: String,
    var readyInMinutes: Int,
    var servings: Int,
    var summary: String,
    var pricePerServing: Float,
    var caloriesPerServing: Int,
    var healthScore: Double,
    var diets: List<String>,
    var extendedIngredients: List<Ingredient>,
)



// 配料类
data class Ingredient(
    var name: String
)