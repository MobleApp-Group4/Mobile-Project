package com.example.todo.viewmodel


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.api.RecipesApi
import com.example.todo.model.Comment
import com.example.todo.model.Recipe
import com.example.todo.model.RecipeDetail
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RecipesViewModel:  ViewModel()  {

    var allRecipes = mutableStateListOf<Recipe>()
        private set


    var recipeDetail = mutableStateOf<RecipeDetail?>(null)
        private set

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _avgRating = MutableStateFlow(0.0)
    val avgRating: StateFlow<Double> = _avgRating.asStateFlow()

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

    fun addComment(recipeId: String, userId: String, rating: Double, text: String) {
        val timestamp = Timestamp.now() // Firestore 原生时间戳

        val recipeRef = Firebase.firestore.collection("recipes").document(recipeId)
        val commentRef = recipeRef.collection("comments").document()

        // 先检查 recipeId 是否存在
        recipeRef.get().addOnSuccessListener { recipeSnapshot ->
            if (!recipeSnapshot.exists()) {
                // 如果不存在，创建一个默认 recipe 文档
                val newRecipe = mapOf("averageRating" to 0.0)
                recipeRef.set(newRecipe)
            }

            Firebase.firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { userDocument ->
                    val userName = userDocument.getString("name") ?: "Unknown"

                    val newComment = mapOf(
                        "userId" to userId,
                        "userName" to userName,  // 使用获取到的 userName
                        "rating" to rating,
                        "content" to text,
                        "timestamp" to timestamp,
                        "likes" to 0
                    )

                    commentRef.set(newComment).addOnSuccessListener {
                        updateRecipeRating(recipeId) // 更新平均评分
                    }.addOnFailureListener { e ->
                        Log.e("Firestore", "Failed to add comment: ${e.message}")
                    }
                }.addOnFailureListener { e ->
                    Log.e("Firestore", "Failed to add user: ${e.message}")
                }
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Failed to fetch recipe: ${e.message}")
        }

    }

    private fun updateRecipeRating(recipeId: String) {
        val commentsRef = Firebase.firestore
            .collection("recipes")
            .document(recipeId)
            .collection("comments")

        commentsRef.get().addOnSuccessListener { snapshot ->
            if (!snapshot.isEmpty) {
                val ratings = snapshot.documents.mapNotNull { it.getDouble("rating") }

                if (ratings.isNotEmpty()) {
                    val avgRating = (ratings.average() * 10).roundToInt() / 10.0  // ✅ 保留 1 位小数

                    Firebase.firestore.collection("recipes").document(recipeId)
                        .set(mapOf("averageRating" to avgRating), SetOptions.merge()) // 允许创建新字段
                        .addOnSuccessListener {
                            Log.d("Firestore", "Successfully updated averageRating: $avgRating")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to update averageRating: ${e.message}")
                        }
                } else {
                    Log.e("Firestore", "No valid ratings found for recipe $recipeId")
                }
            } else {
                Log.e("Firestore", "No comments found for recipe $recipeId")
            }
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Failed to fetch comments: ${e.message}")
        }
    }

    fun getRecipeRating(recipeId: String) {
        Firebase.firestore.collection("recipes")
            .document(recipeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching recipe detail: ${error.message}")
                    return@addSnapshotListener
                }

                snapshot?.let {
                    if (it.exists()) {
                        val avgRating = it.getDouble("averageRating") ?: 0.0
                        _avgRating.value = avgRating
                        Log.d("Firestore", "Fetched averageRating: $avgRating")
                    } else {
                        _avgRating.value = -1.0
                        Log.e("Firestore", "Recipe document does not exist")
                    }
                }
            }
    }

    fun loadComments(recipeId: String) {
        Firebase.firestore.collection("recipes")
            .document(recipeId)
            .collection("comments")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error loading comments: ${error.message}")
                    return@addSnapshotListener
                }


                val newComments = snapshot?.documents?.mapNotNull { it.toObject(Comment::class.java) } ?: emptyList()
                viewModelScope.launch {
                    _comments.emit(newComments)  // emit 确保 UI 更新
                }
                Log.d("Firestore", "Loaded ${newComments.size} comments for recipeId: $recipeId")
            }
    }



}