package com.example.todo.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.api.RecipesApi
import com.example.todo.model.Recipe
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UserViewModel:  ViewModel()  {

    var db = FirebaseFirestore.getInstance()
        private set


    var favoriteRecipes = mutableStateListOf<Recipe>()
        private set

    var cartItems = mutableStateListOf<Recipe>()  // 用于存储购物车中的食谱
        private set

    fun isFavorite(userId: String, recipeId: String, onResult: (Boolean) -> Unit) {
        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(recipeId)
            .get()
            .addOnSuccessListener { document ->
                onResult(document.exists())  // 文档存在，则已收藏
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to check favorite", e)
                onResult(false)
            }
    }

    fun addFavorite(userId: String,recipeId: String) {
        val favoriteData = mapOf(
            "recipeId" to recipeId,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("users")
            .document(userId)  // 🔥 使用固定测试用户 ID
            .collection("favorites")
            .document(recipeId)
            .set(favoriteData)
            .addOnSuccessListener { Log.d("Firestore", "Recipe favorited!") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed to favorite recipe", e) }
    }

    fun removeFavorite(userId: String,recipeId: String) {
        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(recipeId)
            .delete()
            .addOnSuccessListener { Log.d("Firestore", "Recipe unfavorited!") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed to unfavorite recipe", e) }
    }

    fun getFavoriteRecipes(userId: String) {
        db.collection("users")
            .document(userId) //Str
            .collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                val recipeIds = documents.map { it.id } // 获取所有收藏的 recipeId
                favoriteRecipes.clear() // 清空旧数据
                recipeIds.forEach { recipeId ->
                    fetchRecipeById(recipeId) { recipe ->
                        recipe?.let { favoriteRecipes.add(it) } // 获取完整的食谱信息
                    }
                }
                Log.d("Firestore", "Successfully fetched favorite recipes.")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to fetch favorite recipes", e)
            }
    }

    fun addToCart(userId: String, recipeId: String) {
        val cartData = mapOf(
            "recipeId" to recipeId,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("users")
            .document(userId)
            .collection("cart")
            .document(recipeId)
            .set(cartData)
            .addOnSuccessListener { Log.d("Firestore", "Recipe added to cart!") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed to add recipe to cart", e) }
    }


    fun getCartItems(userId: String) {
        db.collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                val recipeIds = documents.map { it.id } // 获取所有购物车中的 recipeId
                cartItems.clear() // 清空旧数据
                recipeIds.forEach { recipeId ->
                    fetchRecipeById(recipeId) { recipe ->
                        recipe?.let { cartItems.add(it) } // 获取完整的食谱信息
                    }
                }
                Log.d("Firestore", "Successfully fetched cart items.")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to fetch cart items", e)
            }
    }

    private fun fetchRecipeById(recipeId: String, onResult: (Recipe?) -> Unit) {
        viewModelScope.launch {
            try {
                val response =
                    RecipesApi.recipesService.getRecipeDetail(recipeId.toInt()) // API 请求
                val recipe = Recipe(
                    id = response.id, //Int
                    title = response.title,
                    image = response.image
                )
                onResult(recipe)
            } catch (e: Exception) {
                Log.e("API", "Failed to fetch recipe by ID: ${e.message}")
                onResult(null)
            }
        }
    }

}
