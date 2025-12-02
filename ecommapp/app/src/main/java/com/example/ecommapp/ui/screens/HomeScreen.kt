package com.example.ecommapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecommapp.data.local.entities.Product
import com.example.ecommapp.ui.components.LoadingIndicator
import com.example.ecommapp.ui.components.ProductCard
import com.example.ecommapp.ui.viewmodel.CartViewModel
import com.example.ecommapp.ui.viewmodel.ProductViewModel
import com.example.ecommapp.ui.viewmodel.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    wishlistViewModel: WishlistViewModel,
    userId: Int
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

    val products by productViewModel.products.collectAsState()
    val categories by productViewModel.categories.collectAsState()
    val selectedCategory by productViewModel.selectedCategory.collectAsState()
    val wishlistProductIds by wishlistViewModel.wishlistProductIds.collectAsState()
    val priceRange by productViewModel.priceRange.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EzyShop", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    productViewModel.updateSearchQuery(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search products...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            productViewModel.updateSearchQuery("")
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true
            )

            // Categories
            if (categories.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { productViewModel.selectCategory(null) },
                            label = { Text("All") }
                        )
                    }
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { productViewModel.selectCategory(category) },
                            label = { Text(category) }
                        )
                    }
                }
            }

            // Products Grid
            if (products.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No products found")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            isInWishlist = wishlistProductIds.contains(product.id),
                            onProductClick = {
                                navController.navigate("product_detail/${product.id}")
                            },
                            onWishlistClick = {
                                wishlistViewModel.toggleWishlist(userId, product)
                            }
                        )
                    }
                }
            }
        }

        // Filter Dialog
        if (showFilterDialog) {
            FilterDialog(
                currentPriceRange = priceRange,
                onDismiss = { showFilterDialog = false },
                onApply = { range ->
                    productViewModel.updatePriceRange(range)
                    showFilterDialog = false
                },
                onClear = {
                    productViewModel.clearFilters()
                    searchQuery = ""
                    showFilterDialog = false
                }
            )
        }
    }
}

@Composable
fun FilterDialog(
    currentPriceRange: ClosedFloatingPointRange<Float>,
    onDismiss: () -> Unit,
    onApply: (ClosedFloatingPointRange<Float>) -> Unit,
    onClear: () -> Unit
) {
    var priceRange by remember { mutableStateOf(currentPriceRange) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filters") },
        text = {
            Column {
                Text("Price Range", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                RangeSlider(
                    value = priceRange,
                    onValueChange = { priceRange = it },
                    valueRange = 0f..50000f,
                    steps = 49
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("₹${priceRange.start.toInt()}")
                    Text("₹${priceRange.endInclusive.toInt()}")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onApply(priceRange) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onClear) {
                    Text("Clear All")
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}