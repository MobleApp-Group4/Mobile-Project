package com.example.todo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo.ui.theme.TodoTheme
import com.example.todo.viewmodel.RecipesViewModel
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val userViewModel: UserViewModel by viewModels()
        val recipeViewModel: RecipesViewModel by viewModels()
        setContent {
            TodoTheme(darkTheme = isSystemInDarkTheme()) {
                AppScaffold(userViewModel, recipeViewModel)
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun TodoPreview() {
//    TodoTheme {
//        AppScaffold(userViewModel, recipeViewModel)
//    }
//}