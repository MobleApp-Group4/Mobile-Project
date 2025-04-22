package com.example.todo.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todo.ui.components.CartItem
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth



// 购物车界面，包含多个 CartItem
@Composable
fun CartScreen(
    modifier:Modifier=Modifier,
    userViewModel: UserViewModel,
    navController: NavController
) {
    // get user info from userViewModel
    val user by userViewModel.user.collectAsState()
    val userId = user?.userId

    if (userId == null) {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
        return
    }

    LaunchedEffect(userId){
        userViewModel.getCartItems(userId)
    }
    val cartItems by userViewModel.cartItems.collectAsState(emptyList())
    val filteredCartItems = cartItems.filter { it.quantity > 0 }


    Box(modifier = modifier.fillMaxSize()){
        LazyColumn(
            modifier = Modifier
                .padding(16.dp) // 给底部按钮留出空间
                .padding(bottom = 60.dp)
//                .verticalScroll(rememberScrollState())
        ) {
            items(filteredCartItems) {cartRecipe ->
                CartItem(
                    cartRecipe = cartRecipe,
                    onQuantityChange = { newQuantity ->
                        if (userId != null) {
                            if (newQuantity > 0) {
                                userViewModel.updateCartItem(userId, cartRecipe.recipeId, newQuantity)
                            } else {
                                userViewModel.removeFromCart(userId, cartRecipe.recipeId)
                            }
                        }
                    },
                    navController = navController
                )
            }
        }

        Button(
            onClick = {
                navController.navigate("checkout")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Place Order")
        }
    }

}


//
//
