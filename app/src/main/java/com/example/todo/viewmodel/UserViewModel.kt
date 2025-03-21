package com.example.todo.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.api.RecipesApi
import com.example.todo.model.CartItem
import com.example.todo.model.Order
import com.example.todo.model.Recipe
import com.example.todo.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class UserViewModel:  ViewModel()  {

    var db = FirebaseFirestore.getInstance()
        private set

    var favoriteRecipes = mutableStateListOf<Recipe>()
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
   private val _user = MutableStateFlow<User?>(null) // 存储当前登录用户
   val user: StateFlow<User?> = _user.asStateFlow()

    // Sign up
    fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let {
                        val userId = it.uid
                        val newUser = User(userId = userId, email = email)
                        saveUserData(newUser) { success ->
                            if (success) {
                                onResult(true, "Successful to save user data")  //  onComplete(true)
                            } else {
                                onResult(false, "Failed to save user data")  //  onComplete(false)
                            }
                        }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    private fun saveUserData(user: User, onComplete: (Boolean) -> Unit) {
        db.collection("users")
            .document(user.userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "User data saved successfully!")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to save user data", e)
                onComplete(false)
            }
    }

    // Sign in
    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId!= null){
                        loadUserData(userId) { user ->
                            if (user != null) {
                                _user.value = user  // ✅ 将用户数据存入 ViewModel
                                Log.d("loginUser", "✅ _user updated: ${_user.value}")
                                onResult(true, "Successful to load user data")
                            } else {
                                onResult(false, "Failed to load user data")
                            }
                        }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    private fun loadUserData(userId: String, onComplete: (User?) -> Unit) {
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    Log.d("loadUserData", "$user")
                    onComplete(user)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to load user data", e)
                onComplete(null)
            }
    }


    fun logout() {
        auth.signOut()
    }


    fun isFavorite(userId:String, recipeId: String, onResult: (Boolean) -> Unit) {
        //val userId = _user.value?.userId ?: return
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

    fun addFavorite(userId:String, recipeId: String) {
//        val userId = _user.value?.userId ?: return
//        Log.d("AddFavorite", "User: ${_user.value}")  // 确保 _user.value 不为空
//        Log.d("AddFavorite", "User ID: $userId") // 添加日志，确认 userId 是否为空
//        val user = _user.value
//        if (user == null) {
//            Log.d("AddFavorite", "User is null, aborting favorite action")
//            return
//        }
//
//        Log.d("AddFavorite", "✅ Adding favorite for User: ${user.userId}")
//
//
//        val userId = user.userId
//        Log.d("AddFavorite", "User: $user")  // 打印 user 对象
//        Log.d("AddFavorite", "User ID: $userId")  // 打印 userId
//        Log.d("AddFavorite", "Recipe ID: $recipeId")  // 打印 recipeId


        val favoriteData = mapOf(
            "recipeId" to recipeId,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("users")
            .document(userId)  //
            .collection("favorites")
            .document(recipeId)
            .set(favoriteData)
            .addOnSuccessListener { Log.d("Firestore", "Recipe favorited!") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed to favorite recipe", e) }
    }

    fun removeFavorite(userId:String, recipeId: String) {
        //val userId = _user.value?.userId ?: return
        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(recipeId)
            .delete()
            .addOnSuccessListener { Log.d("Firestore", "Recipe unfavorited!") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed to unfavorite recipe", e) }
    }

    fun getFavoriteRecipes(userId:String) {
        //val userId = _user.value?.userId ?: return
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

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    fun addToCart( userId:String,recipeId:String,title: String, image: String) {
//        val userId = _user.value?.userId ?: return
        val cartRef = db.collection("users")
            .document(userId)
            .collection("cart")
            .document(recipeId)

        cartRef.get()
            .addOnSuccessListener { document ->
            val currentQuantity = document.getLong("quantity") ?: 0
            val updatedQuantity = currentQuantity + 1 // 每次点击加 1
            val cartData = mapOf(
                "recipeId" to recipeId,
                "title" to title,
                "image" to image,
                "quantity" to updatedQuantity,
                "timestamp" to FieldValue.serverTimestamp()
            )

            // 更新购物车数据
            cartRef.set(cartData,SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("Firestore", "Recipe quantity updated in cart!")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed to update recipe quantity", e)
                }
        } .addOnFailureListener { e ->
            Log.e("Firestore", "Failed to get cart data", e)
        }
    }

    // 加载购物车数据
    fun getCartItems(userId:String) {
//        val userId = _user.value?.userId ?: return
        db.collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                val items = documents.map { document ->
                    CartItem(
                        recipeId = document.getString("recipeId") ?: "",
                        title = document.getString("title") ?: "",
                        image = document.getString("image") ?: "",
                        quantity = document.getLong("quantity")?.toLong() ?: 0
                    )
                }
                _cartItems.value = items  // 更新购物车数据
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to fetch cart items", e)
            }
    }

    // 更新商品数量
    fun updateCartItem(userId:String, recipeId: String, newQuantity: Long) {
//        val userId = _user.value?.userId ?: return
        val cartRef = db.collection("users")
            .document(userId)
            .collection("cart")
            .document(recipeId)

        val cartData = mapOf(
            "quantity" to newQuantity,
            "timestamp" to FieldValue.serverTimestamp()
        )

        cartRef.set(cartData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Firestore", "Recipe quantity updated!")
                getCartItems(userId)  // 更新购物车
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to update recipe quantity", e)
            }
    }

    // 移除商品
    fun removeFromCart(userId:String, recipeId: String) {
//        val userId = _user.value?.userId ?: return
        val cartRef = db.collection("users")
            .document(userId)
            .collection("cart")
            .document(recipeId)

        cartRef.delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Recipe removed from cart!")
                getCartItems(userId)  // 更新购物车
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to remove recipe from cart", e)
            }
    }

    //Order Data
    fun confirmOrder(userId:String, address: String, phoneNumber: String) {
//        val userId = _user.value?.userId ?: return
        // create orderId
        val orderId = System.currentTimeMillis().toString()

        // 获取当前时间（可以格式化为你想要的格式）
        val createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        // 获取购物车数据
        val cartRef = db.collection("users").document(userId).collection("cart")
        cartRef.get()
            .addOnSuccessListener { documents ->
                // 将购物车中的商品转换为 OrderItems
                val orderItems = documents.map { document ->
                    CartItem(
                        recipeId = document.getString("recipeId") ?: "",
                        title = document.getString("title") ?: "",
                        image = document.getString("image") ?: "",
                        quantity = document.getLong("quantity")?.toLong() ?: 0
                    )
                }

                // 创建订单数据
                val orderData = Order(
                    orderId = orderId,
                    userId = userId,
                    status = "Pending", // 假设订单默认状态是 "Pending"
                    createdAt = createdAt,
                    orderItems = orderItems,
                    address = address,
                    phoneNumber = phoneNumber
                )

                // 将订单数据保存到 Firestore
                db.collection("users")
                    .document(userId)
                    .collection("orders")
                    .document(orderId)
                    .set(orderData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Order placed successfully!")

                        // 提交成功后清空购物车
                        clearCart(userId)
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Failed to place order", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to fetch cart items for order", e)
            }
    }

    private fun clearCart(userId:String) {
//        val userId = _user.value?.userId ?: return
        val cartRef = db.collection("users").document(userId).collection("cart")
        cartRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d("Firestore", "Item removed from cart")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to remove item from cart", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to fetch cart items for clearing", e)
            }
    }

    private val _orders = MutableLiveData<List<Order>>(emptyList())
    val orders: LiveData<List<Order>> = _orders

    fun fetchOrders(userId:String) {
//        val userId = _user.value?.userId ?: return
        db.collection("users").document(userId).collection("orders")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val ordersList = it.documents.mapNotNull { document -> document.toObject(Order::class.java) }
                    _orders.value = ordersList
                }
            }
    }

}
