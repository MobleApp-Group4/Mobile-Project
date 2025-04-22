package com.example.todo.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OrderScreen(
    userViewModel: UserViewModel,
    modifier: Modifier,
    navController: NavController
) {
    val user by userViewModel.user.collectAsState()
    val userId = user?.userId
    val role = user?.role

    //check userId
    if (userId == null) {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
        return
    }

    if (role == "admin") {
        Log.e("OderScreen", " $role")
        AdminOrderScreen(modifier,userViewModel)
    } else {
        Log.e("OderScreen", " $role")
        UserOrderScreen(userViewModel, modifier,navController,userId)

    }
}
