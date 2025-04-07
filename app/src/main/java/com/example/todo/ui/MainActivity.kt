package com.example.todo.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import com.example.todo.ui.theme.TodoTheme
import com.example.todo.viewmodel.RecipesViewModel
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import com.example.todo.R
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : ComponentActivity() {
//    private lateinit var auth: FirebaseAuth
    private val credentialManager by lazy { CredentialManager.create(this) }
    private val userViewModel: UserViewModel by viewModels()  // 使用 ViewModel 来管理用户状态

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initiate
        FirebaseApp.initializeApp(this)
//        auth = FirebaseAuth.getInstance()

//        val userViewModel: UserViewModel by viewModels()
        userViewModel.setCredentialManager(credentialManager, this) // 传入 CredentialManager 和 context

        val recipeViewModel: RecipesViewModel by viewModels()


        setContent {
            TodoTheme(darkTheme = isSystemInDarkTheme()) {
                AppScaffold(userViewModel, recipeViewModel)
                Log.d("MainActivity", "userViewModel instance: ${userViewModel.hashCode()}")

            }
        }

    }

//
//
//    // 开始 Google 登录
//    fun signInWithGoogle() {
//        val googleIdOption = GetGoogleIdOption.Builder()
//            .setServerClientId(getString(R.string.default_web_client_id)) // 替换成你的 Web 客户端 ID
//            .setFilterByAuthorizedAccounts(false)
//            .build()
//
//        val request = GetCredentialRequest.Builder()
//            .addCredentialOption(googleIdOption)
//            .build()
//        lifecycleScope.launch {
//            try {
//                val result = credentialManager.getCredential(this@MainActivity, request)
//                handleSignIn(result.credential)
//            } catch (e: Exception) {
//                Log.e("MainActivity", "Google Sign-In failed: ${e.localizedMessage}")
//            }
//        }
//    }
//
//    // 处理 Google 登录结果
//    private fun handleSignIn(credential: Credential) {
//        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
//            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
//        } else {
//            Log.w("MainActivity", "Credential is not of type Google ID!")
//        }
//    }
//
//    // 用 Google Token 登录 Firebase
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Log.d("MainActivity", "signInWithCredential:success")
//                    val user = auth.currentUser
//                    updateUI(user)
//                } else {
//                    Log.w("MainActivity", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
//                }
//            }
//    }
//
//    // 退出登录
//    private fun signOut() {
//        auth.signOut()
//
//        lifecycleScope.launch {
//            try {
//                val clearRequest = ClearCredentialStateRequest()
//                credentialManager.clearCredentialState(clearRequest)
//                updateUI(null)
//            } catch (e: ClearCredentialException) {
//                Log.e("MainActivity", "Couldn't clear user credentials: ${e.localizedMessage}")
//            }
//        }
//    }
//
//    // 更新 UI（你可以替换为自己的 UI 逻辑）
//    private fun updateUI(user: FirebaseUser?) {
//        if (user != null) {
//            Log.d("MainActivity", "User logged in: ${user.displayName}")
//        } else {
//            Log.d("MainActivity", "User not logged in")
//        }
//    }
}


