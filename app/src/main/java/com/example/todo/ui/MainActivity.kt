package com.example.todo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo.ui.theme.TodoTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            TodoTheme {
                AppScaffold()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TodoPreview() {
    TodoTheme {
        AppScaffold()
    }
}