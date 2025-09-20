package com.example.ecomm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecomm.data.models.Product
import com.example.ecomm.viewmodel.MainViewModel
import com.example.ecomm.ui.components.ProductCard

@Composable
fun WishlistScreen(
    viewModel: MainViewModel,
    onProductClick: (Product) -> Unit
) {
    val wishlist by viewModel.wishlist.collectAsState()

    if (wishlist.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your wishlist is empty",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(wishlist) { product ->
                ProductCard(
                    product = product,
                    isInWishlist = true,
                    onProductClick = { onProductClick(product) },
                    onAddToCart = { viewModel.addToCart(product) },
                    onToggleWishlist = { viewModel.toggleWishlist(product) }
                )
            }
        }
    }
}

// Note: ProductCard composable is already defined in ProductListScreen.kt
// You can create a separate components file for shared UI components