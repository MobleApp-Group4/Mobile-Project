package com.example.todo.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OrderScreen(
    userViewModel: UserViewModel = viewModel(),
    modifier: Modifier,
    navController: NavController
) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    if (userId == "3iAMOHgKEIZtPjqb9peubRcjrAR2") {
        AdminOrderScreen(modifier,userViewModel)
    } else {
        if (userId != null) {
            UserOrderScreen(userViewModel, modifier,navController,userId)
        }
    }
}
