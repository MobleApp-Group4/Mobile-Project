package com.example.todo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todo.appbars.BottomBar
import com.example.todo.appbars.DetailBottomBar
import com.example.todo.appbars.ScreenTopBar
import com.example.todo.appbars.TopBar
import com.example.todo.ui.screens.CartScreen
import com.example.todo.ui.screens.FavoriteScreen
import com.example.todo.ui.screens.InfoScreen
import com.example.todo.ui.screens.LoginScreen
import com.example.todo.ui.screens.ProfileScreen
import com.example.todo.ui.screens.RecipeDetailScreen
import com.example.todo.ui.screens.RecipeScreen
import com.example.todo.ui.screens.SettingsScreen


@Composable
fun AppScaffold() {
    val navController = rememberNavController() // 创建 NavController
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    Scaffold(
        topBar = {
            when (currentRoute) {
                "home" -> TopBar(navController)
                "favorites" -> ScreenTopBar("Favorites",navController)
                "profile" -> ScreenTopBar("Profile",navController)
                "settings" -> ScreenTopBar("Settings",navController)
                "info" -> ScreenTopBar("Info",navController)
                "cart" -> ScreenTopBar("Cart",navController)
                "recipes/{recipeId}" -> ScreenTopBar("Recipes",navController)
//                "todos" -> ScreenTopBar("List",navController)
                "todos/{todoId}" -> ScreenTopBar("Todo",navController)
                else -> TopBar(navController)
            }
        },
        // drawerContent = { DrawerContent(navController) },  // 侧边栏内容
        bottomBar = {
            when (currentRoute) {
                "recipes/{recipeId}" -> {
                    // 显示 DetailBottomBar 并传递 recipeId
                    backStackEntry.value?.arguments?.getString("recipeId")?.toInt()
                        ?.let { recipeId ->
                            DetailBottomBar(recipeId = recipeId, navController = navController)
                        }
                }

                else -> {
                    BottomBar(navController)
                }
            }
        },
            // 底部导航栏

        content = { innerPadding ->
            val modifier = Modifier
                .padding(innerPadding)
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable(route = "home") { RecipeScreen(navController,modifier)}
                composable(route = "login") { LoginScreen(modifier) }
                composable(route = "info") { InfoScreen(modifier) }
                composable(route = "settings") { SettingsScreen(modifier) }
                composable(route = "profile") { ProfileScreen(modifier) }
                composable(route = "cart") { CartScreen(modifier) }
                composable(route = "favorites") { FavoriteScreen(navController,modifier) }
                composable("recipes/{recipeId}") { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId")?.toInt() ?: 0
                    RecipeDetailScreen(modifier=modifier,recipeId=recipeId)
                }

            }
        }
    )
}

