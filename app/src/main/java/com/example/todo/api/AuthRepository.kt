package com.example.todo.api//package com.example.todo.api
//
//import android.util.Log
//import com.example.todo.model.User
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.tasks.await
//
//class AuthRepository {
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//
//    private val _user = MutableStateFlow<User?>(null)
//    val user: StateFlow<User?> = _user.asStateFlow()
//
//    // 获取当前用户
//    fun getCurrentUser(): FirebaseUser? = auth.currentUser
//
//    // 用 Google Token 进行 Firebase 认证
//    suspend fun signInWithGoogle(idToken: String): User? {
//        return try {
//            val credential = GoogleAuthProvider.getCredential(idToken, null)
//            val authResult = auth.signInWithCredential(credential).await()
//            val firebaseUser = authResult.user ?: return null
//
//            val user = User(
//                userId = firebaseUser.uid,
//                email = firebaseUser.email ?: "",
//                name = firebaseUser.displayName ?: "",
//                avatar = firebaseUser.photoUrl?.toString() ?: "",
//                phoneNumber = firebaseUser.phoneNumber ?: "",
//            )
//
//            saveUserToFirestore(user)
//            _user.value = user
//            user
//        } catch (e: Exception) {
//            Log.e("AuthRepository", "Google 登录失败: ${e.localizedMessage}")
//            null
//        }
//    }
//
//
//    private fun saveUserToFirestore(user: User) {
//        db.collection("users").document(user.userId).set(user)
//    }
//
//    // 退出登录
//    fun signOut() {
//        auth.signOut()
//    }
//}