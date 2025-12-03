package com.example.ecommapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommapp.ui.viewmodel.AuthViewModel
import com.example.ecommapp.ui.viewmodel.CartViewModel
import com.example.ecommapp.ui.viewmodel.OrderViewModel
import com.example.ecommapp.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    onOrderPlaced: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalAmount by cartViewModel.totalAmount.collectAsState()
    val addresses by profileViewModel.addresses.collectAsState()

    var selectedAddressId by remember { mutableStateOf<Int?>(null) }
    var isPlacingOrder by remember { mutableStateOf(false) }
    var orderError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Load addresses when screen opens
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            profileViewModel.loadAddresses(user.id)
            cartViewModel.loadCartItems(user.id)
        }
    }

    // Auto-select default address
    LaunchedEffect(addresses) {
        if (selectedAddressId == null && addresses.isNotEmpty()) {
            selectedAddressId = addresses.firstOrNull { it.isDefault }?.id ?: addresses.firstOrNull()?.id
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Delivery Address",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (addresses.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No address found",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = "Please add a delivery address from profile",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    } else {
                        addresses.forEach { address ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                onClick = { selectedAddressId = address.id },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedAddressId == address.id)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else Color.White
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedAddressId == address.id,
                                        onClick = { selectedAddressId = address.id }
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = address.fullName,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = address.phone,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "${address.addressLine}, ${address.city}, ${address.state} - ${address.pincode}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    if (address.isDefault) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(
                                                text = "DEFAULT",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            if (cartItems.isEmpty()) {
                                Text(
                                    text = "No items in cart",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                cartItems.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${item.productName} (x${item.quantity})",
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "₹${String.format("%.0f", item.productPrice * item.quantity)}",
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Subtotal:",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "₹${String.format("%.0f", totalAmount)}",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Delivery:",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "FREE",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF4CAF50)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total:",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "₹${String.format("%.0f", totalAmount)}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    orderError?.let { error ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = error,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    // Extra space for bottom button
                    Spacer(modifier = Modifier.height(100.dp))
                }

                // Fixed bottom button
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Amount",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "₹${String.format("%.0f", totalAmount)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Button(
                            onClick = {
                                if (selectedAddressId != null && cartItems.isNotEmpty()) {
                                    isPlacingOrder = true
                                    orderError = null
                                    scope.launch {
                                        orderViewModel.placeOrder(
                                            userId = currentUser?.id ?: 0,
                                            cartItems = cartItems,
                                            totalAmount = totalAmount,
                                            addressId = selectedAddressId!!,
                                            onSuccess = {
                                                isPlacingOrder = false
                                                onOrderPlaced()
                                            },
                                            onError = { error ->
                                                isPlacingOrder = false
                                                orderError = error
                                            }
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.height(56.dp),
                            enabled = selectedAddressId != null && cartItems.isNotEmpty() && !isPlacingOrder
                        ) {
                            if (isPlacingOrder) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Placing...")
                            } else {
                                Text("Place Order")
                            }
                        }
                    }
                }
            }
        }
    }
}