package com.example.ecommapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ecommapp.data.local.entities.Product
import com.example.ecommapp.data.local.entities.WishlistItem
import com.example.ecommapp.ui.viewmodel.CartViewModel
import com.example.ecommapp.ui.viewmodel.ProductViewModel
import com.example.ecommapp.ui.viewmodel.WishlistViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    navController: NavHostController,
    wishlistViewModel: WishlistViewModel,
    cartViewModel: CartViewModel,
    userId: Int,
    productViewModel: ProductViewModel = viewModel()
) {
    val wishlistItems by wishlistViewModel.wishlistItems.collectAsState()
    val products by productViewModel.products.collectAsState()

    var showAddedToCart by remember { mutableStateOf(false) }

    LaunchedEffect(showAddedToCart) {
        if (showAddedToCart) {
            kotlinx.coroutines.delay(2000)
            showAddedToCart = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Wishlist", fontWeight = FontWeight.Bold) }
            )
        },
        snackbarHost = {
            if (showAddedToCart) {
                Snackbar(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Added to cart!")
                }
            }
        }
    ) { paddingValues ->
        if (wishlistItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Your wishlist is empty",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { navController.navigate("home") }) {
                        Text("Browse Products")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wishlistItems) { wishlistItem ->
                    val product = products.find { it.id == wishlistItem.productId }
                    if (product != null) {
                        WishlistItemCard(
                            wishlistItem = wishlistItem,
                            product = product,
                            onClick = { navController.navigate("product_detail/${product.id}") },
                            onAddToCart = {
                                cartViewModel.addToCart(userId, product)
                                showAddedToCart = true
                            },
                            onRemove = {
                                wishlistViewModel.removeFromWishlist(wishlistItem)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WishlistItemCard(
    wishlistItem: WishlistItem,
    product: Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        wishlistItem.productImage,
        "drawable",
        context.packageName
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(
                    id = if (imageResId != 0) imageResId else android.R.drawable.ic_menu_gallery
                ),
                contentDescription = wishlistItem.productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = wishlistItem.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                val finalPrice = if (product.discount > 0) {
                    product.price * (1 - product.discount / 100.0)
                } else {
                    product.price
                }

                Text(
                    text = "₹${String.format("%.0f", finalPrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onAddToCart,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Add to Cart",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add to Cart", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onRemove,
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}