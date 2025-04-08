package com.example.todo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todo.ui.appbars.BottomBar
import com.example.todo.ui.appbars.ScreenTopBar
import com.example.todo.ui.appbars.TopBar
import com.example.todo.ui.screens.AdminOrderScreen
import com.example.todo.ui.screens.CartScreen
import com.example.todo.ui.screens.CheckoutScreen
import com.example.todo.ui.screens.FavoriteScreen
import com.example.todo.ui.screens.InfoScreen
import com.example.todo.ui.screens.LoginScreen
import com.example.todo.ui.screens.OrderScreen
import com.example.todo.ui.screens.ProfileScreen
import com.example.todo.ui.screens.RecipeDetailScreen
import com.example.todo.ui.screens.RecipeScreen
import com.example.todo.ui.screens.SettingsScreen
import com.example.todo.viewmodel.RecipesViewModel
import com.example.todo.viewmodel.UserViewModel


@Composable
fun AppScaffold(
    userViewModel: UserViewModel,
    recipeViewModel: RecipesViewModel
) {
    val navController = rememberNavController() // 创建 NavController
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() } // create SnackbarHostState
    //val coroutineScope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            when (currentRoute) {
                "home" -> TopBar(navController)
                "favorites" -> ScreenTopBar("Favorites",navController)
                "profile" -> ScreenTopBar("Profile",navController)
                "settings" -> ScreenTopBar("Settings",navController)
                "info" -> ScreenTopBar("Info",navController)
                "cart" -> ScreenTopBar("Cart",navController)
                "recipes/{recipeId}" -> ScreenTopBar("Recipes",navController)
                "checkout" -> ScreenTopBar("Confirm Orders",navController)
                "orders" -> ScreenTopBar("Orders",navController)
                "all_orders" -> ScreenTopBar("Manage All Orders",navController)
                else -> TopBar(navController)
            }
        },
        // drawerContent = { DrawerContent(navController) },  // 侧边栏内容
        bottomBar = {
            when (currentRoute) {
                "recipes/{recipeId}" -> {
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
                composable(route = "home") { RecipeScreen(navController,modifier,recipeViewModel)}
                composable(route = "login") { LoginScreen(navController=navController,modifier=modifier,userViewModel=userViewModel) }
                composable(route = "info") { InfoScreen(modifier) }
                composable(route = "settings") { SettingsScreen(modifier) }
                composable(route = "profile") { ProfileScreen(modifier,userViewModel) }
                composable(route = "cart") { CartScreen(navController=navController,modifier=modifier,userViewModel = userViewModel) }
                composable(route = "checkout") { CheckoutScreen(navController=navController,modifier=modifier,userViewModel = userViewModel) }
                composable(route = "orders") { OrderScreen(navController=navController,modifier=modifier,userViewModel = userViewModel) }
//                composable(route = "all_orders") { AdminOrderScreen(modifier=modifier) }
                composable(route = "favorites") { FavoriteScreen(navController,modifier,userViewModel,recipeViewModel) }
                composable("recipes/{recipeId}") { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId")?.toInt() ?: 0
                    RecipeDetailScreen(navController=navController,modifier=modifier,recipeId=recipeId,recipeViewModel = recipeViewModel,
                        userViewModel = userViewModel)
                }

            }
        }
    )
}

