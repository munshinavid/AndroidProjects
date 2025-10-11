package com.example.ezy.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.ezy.screens.*
import com.example.ezy.utils.PrefsManager
import kotlinx.coroutines.flow.first

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        val token = PrefsManager.token.first()
        startDestination = if (token != null) "home" else "login"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
            ProductDetailScreen(navController, productId)
        }
        composable("cart") { CartScreen(navController) }
        composable("checkout") { CheckoutScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("orders") { OrdersScreen(navController) }
    }
}