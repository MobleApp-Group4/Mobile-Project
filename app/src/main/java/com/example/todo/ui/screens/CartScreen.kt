package com.example.todo.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo.ui.components.CartItem
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth



// 购物车界面，包含多个 CartItem
@Composable
fun CartScreen(
    modifier:Modifier=Modifier,
    userViewModel: UserViewModel = viewModel(),
    navController: NavController
) {
    // 示例数据（通常应从 ViewModel 或 Repository 获取）
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    LaunchedEffect(userId){
        if (userId != null) {
            userViewModel.getCartItems(userId)
        }
    }
    val cartItems by userViewModel.cartItems.observeAsState(emptyList())
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
