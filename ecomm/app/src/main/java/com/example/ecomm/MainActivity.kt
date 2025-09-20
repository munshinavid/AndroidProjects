// File: MainActivity.kt
package com.example.ecomm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecomm.data.repository.EcommerceRepository
import com.example.ecomm.ui.screens.*
import com.example.ecomm.ui.theme.EcommTheme
import com.example.ecomm.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommTheme {
                EcommerceApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Login : Screen("login", "Login", Icons.Default.Person)
    object Products : Screen("products", "Products", Icons.Default.Home)
    object Cart : Screen("cart", "Cart", Icons.Default.ShoppingCart)
    object Wishlist : Screen("wishlist", "Wishlist", Icons.Default.Favorite)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object ProductDetail : Screen("product/{productId}", "Product", Icons.Default.Info)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcommerceApp() {
    val navController = rememberNavController()
    val viewModel = remember { MainViewModel(EcommerceRepository()) }
    val currentUser by viewModel.currentUser.collectAsState()

    val bottomNavItems = listOf(
        Screen.Products,
        Screen.Cart,
        Screen.Wishlist,
        Screen.Profile
    )

    // Determine start destination based on user state
    val startDestination = if (currentUser == null) Screen.Login.route else Screen.Products.route

    Scaffold(
        bottomBar = {
            if (currentUser != null) {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route, // Always start with login
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Products.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Products.route) {
                ProductListScreen(
                    viewModel = viewModel,
                    onProductClick = { product ->
                        navController.navigate("product/${product.id}")
                    }
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    viewModel = viewModel,
                    onCheckout = {
                        // Handle checkout functionality
                    }
                )
            }

            composable(Screen.Wishlist.route) {
                WishlistScreen(
                    viewModel = viewModel,
                    onProductClick = { product ->
                        navController.navigate("product/${product.id}")
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToOrders = {
                        // Navigate to orders screen when implemented
                    },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.ProductDetail.route) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 0
                val products by viewModel.products.collectAsState()
                val product = products.find { it.id == productId }

                product?.let {
                    ProductDetailScreen(
                        product = it,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                } ?: run {
                    // If product not found, go back to products
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}