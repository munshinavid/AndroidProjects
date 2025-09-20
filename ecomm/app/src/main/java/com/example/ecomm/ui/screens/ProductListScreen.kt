package com.example.ecomm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.ecomm.data.models.Product
import com.example.ecomm.viewmodel.MainViewModel
import com.example.ecomm.ui.components.ProductCard

@Composable
fun ProductListScreen(
    viewModel: MainViewModel,
    onProductClick: (Product) -> Unit
) {
    val products by viewModel.products.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                isInWishlist = viewModel.isInWishlist(product.id),
                onProductClick = { onProductClick(product) },
                onAddToCart = { viewModel.addToCart(product) },
                onToggleWishlist = { viewModel.toggleWishlist(product) }
            )
        }
    }
}