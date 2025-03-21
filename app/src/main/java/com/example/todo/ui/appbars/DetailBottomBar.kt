package com.example.todo.ui.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.todo.viewmodel.UserViewModel

@Composable
fun DetailBottomBar(
    navController: NavController,
    userId: String,
    recipeId: Int,
    title: String,
    image: String,
    userViewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
    ) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    // Tab items for navigation
    val tabs = listOf(
        TabItem("Home", Icons.Filled.Home, route = "home"),
        TabItem("Favorite", Icons.Filled.Favorite, route = "favorites"),
        TabItem("Cart", Icons.Filled.ShoppingCart, route = "cart")
    )

    //var buttonColor by remember { mutableStateOf(Color(0xFFFFA500)) }

    BottomAppBar(
        modifier = modifier.fillMaxWidth()
    ) {
        // First two buttons are navigation buttons (Home and Cart)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),  // Allow navigation buttons to take available space
            horizontalArrangement = Arrangement.Start // Align buttons to the left
        ) {
            // First two buttons are navigation buttons (Home and Cart)
            tabs.forEach { tab ->
                val selected = tab.route == backStackEntry.value?.destination?.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigate(tab.route) },
                    label = { Text(tab.label) },
                    icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                    alwaysShowLabel = true
                )
            }
        }

        // To separate the cart button from "Add to Cart" button
        //val interactionSource = remember { MutableInteractionSource() }
        // Add "Add to Cart" button
        TextButton(
            onClick = {
                // Execute logic to add to cart, for example:
                userViewModel.addToCart(userId = userId, recipeId = recipeId.toString(),title = title,
                    image = image)
            },
            modifier = Modifier
                .background(Color(0xFFFFD700)) // Background color for button
                .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust padding for smaller size
                .height(36.dp) // Set a fixed height for the button
                .clip(RoundedCornerShape(16.dp)) // Rounded corners for the button
//                .pointerInput(Unit) {
//                    detectTapGestures(
//                        onPress = {
//                            buttonColor = Color(0xFFFF4500)
//                        },
//                        onRelease = {
//                            buttonColor = Color(0xFFFFA500)
//                        }
//                    )
//                }
        ) {
            Text(
                text = "Add to Cart",
                color = Color.White // Text color
            )
        }
    }
}
