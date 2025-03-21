package com.example.todo.ui.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomBar(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val tabs = listOf(
        TabItem("Home", Icons.Filled.Home,route="home"),
        //TabItem("Category", Icons.AutoMirrored.Filled.List, route = "favorites"),
        TabItem("Favorite", Icons.Filled.Favorite, route = "favorites"),
        TabItem("Cart", Icons.Filled.ShoppingCart, route = "cart"),
        TabItem("Order", Icons.Filled.Receipt, route = "orders"),
        TabItem("Profile",Icons.Filled.AccountCircle,route="profile")
    )

    NavigationBar {
        tabs.forEach{tab ->
            val selected = tab.route ===
                    backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {navController.navigate(tab.route)},
                label = {Text(tab.label)},
                icon = {Icon(
                    imageVector = tab.icon,
                    contentDescription = null
                )}
            )
        }
    }
}

