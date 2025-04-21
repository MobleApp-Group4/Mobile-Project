package com.example.todo.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.api.RecipesApi
import com.example.todo.model.CartItem
import com.example.todo.model.Order
import com.example.todo.model.Recipe
import com.example.todo.model.User
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import com.example.todo.R
import androidx.credentials.GetCredentialRequest
import android.content.Context
import androidx.credentials.CredentialManager
import java.util.UUID
import androidx.credentials.CustomCredential
import com.google.firebase.Firebase
import com.google.firebase.storage.storage


class UserViewModel:  ViewModel()  {

    private var db = FirebaseFirestore.getInstance()
    private lateinit var credentialManager: CredentialManager
    private lateinit var appContext: Context

    var favoriteRecipes = mutableStateListOf<Recipe>()
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _user = MutableStateFlow<User?>(null) // 存储当前登录用户
    val user: StateFlow<User?> = _user.asStateFlow()


    private val _isLoggedIn = MutableStateFlow(false)  // ✅ 用 StateFlow 代替 mutableStateOf
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        Log.d("UserViewModel", "ViewModel instance created: ${this.hashCode()}")
    }

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
        val userDocRef = db.collection("users").document(user.userId)

        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // 已存在，不覆盖，只调用完成
                    Log.d("Firestore", "User already exists, skip overwriting.")
                    onComplete(true)
                } else {
                    // 不存在，首次登录，写入完整数据
                    userDocRef.set(user)
                        .addOnSuccessListener {
                            Log.d("Firestore", "User created successfully.")
                            onComplete(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to create user", e)
                            onComplete(false)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to check user existence", e)
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
                        loadUserData(userId)
                        _isLoggedIn.value = true
                        Log.d("loginUser", "${_isLoggedIn.value}")
                        onResult(true, null)
                    }else {
                        onResult(false, "User ID is null after login")
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

     fun loadUserData(userId: String) {
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    _user.value = user
                    Log.d("loadUserData", "$user")
                } else {
                    _user.value = null
                    Log.e("loadUserData", "No user data")
                }
            }
            .addOnFailureListener { e ->
                _user.value = null
                Log.e("Firestore", "Failed to load user data", e)
            }
    }

    fun updateUser(updatedUser: User, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        val userId = updatedUser.userId
        if (userId.isEmpty()) {
            onResult(false, "User ID is empty")
            return
        }

        db.collection("users").document(userId)
            .set(updatedUser)
            .addOnSuccessListener {
                _user.value = updatedUser
                Log.d("updateUser", "✅ Update successfully: $updatedUser")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e("updateUser", "Fail to update", e)
                onResult(false, e.message)
            }
    }

    fun updateAvatar(userId: String, imageUri: Uri, onResult: (Boolean, String?) -> Unit) {
        val storageRef = Firebase.storage.reference.child("avatars/$userId.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    db.collection("users").document(userId)
                        .update("avatar", downloadUrl.toString())
                        .addOnSuccessListener {
                            _user.value = _user.value?.copy(avatar = downloadUrl.toString())
                            Log.d("updateAvatar", "Update Avatar Successfully: $downloadUrl")
                            onResult(true, null)
                        }
                        .addOnFailureListener { e ->
                            Log.e("updateAvatar", " Firestore fail", e)
                            onResult(false, e.message)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("updateAvatar", "Fail to update", e)
                onResult(false, e.message)
            }
    }




    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        Log.d("logout", "User logged out, isLoggedIn = ${_isLoggedIn.value}")
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
    fun setCredentialManager(manager: CredentialManager, context: Context) {
        credentialManager = manager
        appContext = context.applicationContext
    }

    fun signInWithGoogle(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)  // 显示所有可用账号
                .setServerClientId("970461204290-5otupsb41g98v1a13dovmn3ist7ggqu4.apps.googleusercontent.com")       // 替换成你的 Web Client ID
                .setAutoSelectEnabled(false)
                .setNonce(UUID.randomUUID().toString())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = appContext
                )

                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    if (idToken != null) {
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser = auth.currentUser
                                    firebaseUser?.let {
                                        val userId = it.uid
                                        val newUser = User(userId = userId, email = it.email ?: "")
                                        saveUserData(newUser) { success ->
                                            if (success) {
                                                _user.value = newUser
                                                _isLoggedIn.value = true
                                                onResult(true, null)
                                            } else {
                                                onResult(false, "Failed to save user")
                                            }
                                        }
                                    }
                                } else {
                                    onResult(false, task.exception?.message)
                                }
                            }
                    }
                } else {
                    onResult(false, "No valid Google credential")
                }
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    //Favorites

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

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart( userId:String,recipeId:String,title: String, image: String) {
//        val userId = _user.value?.userId ?: return
        viewModelScope.launch {
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
    }

    // 加载购物车数据
    fun getCartItems(userId:String) {
//        val userId = _user.value?.userId ?: return
        viewModelScope.launch {
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
    }

    // 更新商品数量
    fun updateCartItem(userId:String, recipeId: String, newQuantity: Long) {
//        val userId = _user.value?.userId ?: return
        viewModelScope.launch {
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
    }

    // 移除商品
    fun removeFromCart(userId:String, recipeId: String) {
//        val userId = _user.value?.userId ?: return
        viewModelScope.launch {
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
    }

    //Order Data
    fun confirmOrder(userId:String, address: String, phoneNumber: String, selectedTimeSlot: String,
                     selectedDate: String,note:String) {
//        val userId = _user.value?.userId ?: return
        viewModelScope.launch {
            // create orderId
            val orderId = System.currentTimeMillis().toString()
            // get time
            val createdAt = Timestamp.now() // ✅ 使用 Firestore Timestamp
            // get cart
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
                        phoneNumber = phoneNumber,
                        timeSlot = selectedTimeSlot,
                        selectedDate = selectedDate,
                        note = note
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
    }

    private fun clearCart(userId:String) {
//        val userId = _user.value?.userId ?: return
        viewModelScope.launch {
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
    }

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    fun getMyOrders(userId:String) {
//        val userId = _user.value?.userId ?: return
        viewModelScope.launch {
            db.collection("users").document(userId).collection("orders")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, _ ->
                    snapshot?.let {
                        val ordersList = it.documents.mapNotNull { document -> document.toObject(Order::class.java) }
                        _orders.value = ordersList
                    }
                }
        }
    }

    fun removeMyOrder(userId: String, orderId: String) {
        val orderRef = db.collection("users")
            .document(userId)
            .collection("orders")
            .document(orderId)

        orderRef.delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Order $orderId deleted successfully")
                getMyOrders(userId)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to delete order $orderId", e)
            }
    }

    //manage orders
    private val _allOrders = MutableStateFlow<List<Order>>(emptyList())
    val allOrders: StateFlow<List<Order>> = _allOrders.asStateFlow()

    fun getAllOrders() {
        viewModelScope.launch {
            db.collectionGroup("orders") // 🔥 直接查询所有 orders
                .get()
                .addOnSuccessListener { snapshot ->
                    val allOrdersList = snapshot.documents.mapNotNull { it.toObject(Order::class.java) }

                    // 更新 StateFlow
                    _allOrders.value = allOrdersList

                    // 打印订单总数
                    Log.d("Firestore", "Total orders fetched: ${allOrdersList.size}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed to fetch all orders", e)
                }
        }
    }


    fun updateOrderStatus(userId: String, orderId: String, newStatus: String) {
        val orderRef = db.collection("users")
            .document(userId)
            .collection("orders")
            .document(orderId)

        orderRef.update("status", newStatus)
            .addOnSuccessListener {
                Log.d("Firestore", "Order status updated to $newStatus")
                getNewOrders()
                getAllOrders()

            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to update order status", e)
            }
    }

    fun updateOrderNote(userId: String, orderId: String, note: String) {
        val orderRef = db.collection("users").document(userId)
            .collection("orders").document(orderId)

        orderRef.update("note", note)
            .addOnSuccessListener {
                Log.d("Firestore", "Note updated successfully")
                getAllOrders()  // 更新列表
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to update note", e)
            }
    }
    private val _newOrders = MutableStateFlow<List<Order>>(emptyList())
    val newOrders: StateFlow<List<Order>> = _newOrders.asStateFlow()

    private val _newOrderCount = MutableStateFlow(0)
    val newOrderCount: StateFlow<Int> = _newOrderCount.asStateFlow()

    fun getNewOrders() {
        val oneDayAgo = Timestamp.now().seconds - (24 * 60 * 60) // 计算 24 小时前的 Unix 时间戳

        viewModelScope.launch {
            db.collectionGroup("orders") // 🔥 直接查询所有 orders
                .whereEqualTo("status", "Pending") // 只查询 Pending 状态
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(oneDayAgo, 0)) // 只获取一天内的订单
                .get()
                .addOnSuccessListener { snapshot ->
                    val newOrdersList = snapshot.documents.mapNotNull { it.toObject(Order::class.java) }

                    // 更新 StateFlow
                    _newOrders.value = newOrdersList

                    // 打印新订单数量
                    Log.d("Firestore", "New orders in last 24 hours: ${newOrdersList.size}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed to fetch new orders", e)
                }
        }
    }

    fun removeAdminOrder(userId: String, orderId: String) {
        val orderRef = db.collection("users")
            .document(userId)
            .collection("orders")
            .document(orderId)

        orderRef.delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Order $orderId deleted successfully")
                getAllOrders()
                getNewOrders()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to delete order $orderId", e)
            }
    }

}
