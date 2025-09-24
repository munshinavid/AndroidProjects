package com.example.ezy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.ezy.ui.screens.*
import com.example.ezy.viewmodel.*

@Composable
fun Navigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val isLoggedIn = authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn.value) "home" else "auth",
        modifier = modifier
    ) {
        composable("auth") {
            AuthScreen(
                authViewModel = authViewModel,
                onNavigateToHome = { navController.navigate("home") }
            )
        }

        composable("home") {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onProductClick = { productId ->
                    navController.navigate("product/$productId")
                },
                onCartClick = { navController.navigate("cart") },
                onProfileClick = { navController.navigate("profile") },
                onWishlistClick = { navController.navigate("wishlist") }
            )
        }

        composable(
            "product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val productViewModel: ProductViewModel = viewModel()
            ProductDetailScreen(
                productId = productId,
                viewModel = productViewModel,
                onBackClick = { navController.popBackStack() },
                onCartClick = { navController.navigate("cart") }
            )
        }

        composable("cart") {
            val cartViewModel: CartViewModel = viewModel()
            CartScreen(
                viewModel = cartViewModel,
                onBackClick = { navController.popBackStack() },
                onCheckoutClick = { navController.navigate("checkout") }
            )
        }

        composable("checkout") {
            val cartViewModel: CartViewModel = viewModel()
            CheckoutScreen(
                viewModel = cartViewModel,
                onBackClick = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        composable("profile") {
            val profileViewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                viewModel = profileViewModel,
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("wishlist") {
            val wishlistViewModel: WishlistViewModel = viewModel()
            WishlistScreen(
                viewModel = wishlistViewModel,
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("product/$productId")
                }
            )
        }
    }
}