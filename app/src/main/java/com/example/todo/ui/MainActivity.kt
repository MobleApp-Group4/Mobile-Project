package com.example.todo.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.todo.ui.theme.TodoTheme
import com.example.todo.viewmodel.RecipesViewModel
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.FirebaseApp
import androidx.credentials.CredentialManager



class MainActivity : ComponentActivity() {
    private val credentialManager by lazy { CredentialManager.create(this) }
    val userViewModel: UserViewModel by viewModels()  // 使用 ViewModel 来管理用户状态

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initiate
        FirebaseApp.initializeApp(this)

        userViewModel.setCredentialManager(credentialManager, this) // 传入 CredentialManager 和 context

        val recipeViewModel: RecipesViewModel by viewModels()

        Log.d("MainActivity", "userViewModel instance: ${userViewModel.hashCode()}")

        setContent {
            TodoTheme(darkTheme = isSystemInDarkTheme()) {
                AppScaffold(userViewModel, recipeViewModel)

            }
        }

    }

}


