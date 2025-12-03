package com.example.ecommapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommapp.ui.components.LoadingIndicator
import com.example.ecommapp.ui.theme.DiscountRed
import com.example.ecommapp.ui.viewmodel.AuthViewModel
import com.example.ecommapp.ui.viewmodel.CartViewModel
import com.example.ecommapp.ui.viewmodel.ProductViewModel
import com.example.ecommapp.ui.viewmodel.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onBackClick: () -> Unit,
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    wishlistViewModel: WishlistViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val product by productViewModel.getProductById(productId).collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val wishlistProductIds by wishlistViewModel.wishlistProductIds.collectAsState()
    val context = LocalContext.current

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
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (product != null) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                currentUser?.let { user ->
                                    product?.let { prod ->
                                        cartViewModel.addToCart(user.id, prod)
                                        showAddedToCart = true
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Add to Cart")
                        }

                        IconButton(
                            onClick = {
                                currentUser?.let { user ->
                                    product?.let { prod ->
                                        wishlistViewModel.toggleWishlist(user.id, prod)
                                    }
                                }
                            },
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                imageVector = if (wishlistProductIds.contains(productId))
                                    Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (wishlistProductIds.contains(productId))
                                    DiscountRed else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
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
        if (product == null) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                val imageResId = context.resources.getIdentifier(
                    product!!.imageRes,
                    "drawable",
                    context.packageName
                )

                Box {
                    Image(
                        painter = painterResource(
                            id = if (imageResId != 0) imageResId else android.R.drawable.ic_menu_gallery
                        ),
                        contentDescription = product!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    if (product!!.discount > 0) {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .background(DiscountRed, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${product!!.discount}% OFF",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = product!!.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            modifier = Modifier
                                .background(
                                    Color(0xFF4CAF50),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${product!!.rating}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${product!!.reviewCount} Reviews",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (product!!.discount > 0) {
                            val discountedPrice = product!!.price * (1 - product!!.discount / 100.0)
                            Text(
                                text = "₹${String.format("%.0f", discountedPrice)}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "₹${String.format("%.0f", product!!.price)}",
                                fontSize = 18.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        } else {
                            Text(
                                text = "₹${String.format("%.0f", product!!.price)}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = product!!.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Category",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = product!!.category,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "In Stock",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "${product!!.stock} units",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}