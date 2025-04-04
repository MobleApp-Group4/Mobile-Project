package com.example.todo.model

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser


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
//    val createdAt: String = "",
    val createdAt: Timestamp = Timestamp.now(), // ✅ 修改为 Firestore Timestamp
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
)

fun FirebaseUser.toUser(): User {
    return User(
        userId = this.uid,
        email = this.email ?: "",
        gender = "", // 你可以从 Firestore 或 Realtime Database 获取更多信息
        birthday = "",
        createdAt = "", // 根据需要可以从 Firebase 获取创建时间
        address = "",
        name = this.displayName ?: "",
        avatar = this.photoUrl?.toString() ?: "", // 获取用户的头像 URL
        phoneNumber = this.phoneNumber ?: ""
    )
}

data class Comment(
    val userId: String = "",
    val userName: String = "",
    val rating: Double = 0.0,
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val likes: Long = 0,

    )



// 配料类
data class Ingredient(
    var name: String
)