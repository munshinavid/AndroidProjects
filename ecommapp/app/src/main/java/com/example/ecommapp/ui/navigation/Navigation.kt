package com.example.ecommapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecommapp.ui.screens.*
import com.example.ecommapp.ui.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (authState is com.example.ecommapp.ui.viewmodel.AuthState.Authenticated) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen(
                parentNavController = navController,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("checkout") {
            CheckoutScreen(
                onBackClick = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate("orders") {
                        popUpTo("main")
                    }
                }
            )
        }

        composable("orders") {
            OrdersScreen(onBackClick = { navController.popBackStack() })
        }

        composable("addresses") {
            AddressScreen(onBackClick = { navController.popBackStack() })
        }
    }
}