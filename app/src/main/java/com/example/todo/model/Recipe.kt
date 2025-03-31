package com.example.todo.model


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

data class CartItem(
    val recipeId: String = "",
    val title: String = "",
    val image: String = "",
    val quantity: Long = 0
)

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val status: String = "",
    val createdAt: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val orderItems: List<CartItem> = emptyList()
)

data class User(
    val userId: String = "",
    val email: String = "",
    val gender: String = "",
    val birthday: String = "",
    val createdAt: String = "",
    val address: String = "",
    val name: String = "",
    val avatar: String = "",
    val phoneNumber: String = "",
//    val favorites: List<Recipe> = emptyList(),
//    val cart: List<CartItem> = emptyList(),
//    val orders: List<Order> = emptyList()
)




// 配料类
data class Ingredient(
    var name: String
)