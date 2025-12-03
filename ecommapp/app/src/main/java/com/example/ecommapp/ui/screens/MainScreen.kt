package com.example.ecommapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecommapp.ui.components.BottomNavBar
import com.example.ecommapp.ui.viewmodel.*

@Composable
fun MainScreen(
    parentNavController: NavHostController,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val authViewModel: AuthViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val wishlistViewModel: WishlistViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            cartViewModel.loadCartItems(user.id)
            wishlistViewModel.loadWishlist(user.id)
            orderViewModel.loadOrders(user.id)
            profileViewModel.loadAddresses(user.id)
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(
                    navController = parentNavController,
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    wishlistViewModel = wishlistViewModel,
                    userId = currentUser?.id ?: 0
                )
            }

            composable("cart") {
                CartScreen(
                    navController = parentNavController,
                    cartViewModel = cartViewModel,
                    userId = currentUser?.id ?: 0
                )
            }

            composable("wishlist") {
                WishlistScreen(
                    navController = parentNavController,
                    wishlistViewModel = wishlistViewModel,
                    cartViewModel = cartViewModel,
                    userId = currentUser?.id ?: 0
                )
            }

            composable("profile") {
                ProfileScreen(
                    navController = parentNavController,
                    authViewModel = authViewModel,
                    orderViewModel = orderViewModel,
                    profileViewModel = profileViewModel,
                    onLogout = onLogout,
                    userId = currentUser?.id ?: 0
                )
            }
        }
    }
}