// File: data/repository/EcommerceRepository.kt
package com.example.ecomm.data.repository

import com.example.ecomm.data.models.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EcommerceRepository {
    // For future backend integration, inject ApiService here
    // private val apiService: ApiService

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _wishlist = MutableStateFlow<List<Product>>(emptyList())
    val wishlist: StateFlow<List<Product>> = _wishlist

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    init {
        loadMockProducts()
    }

    private fun loadMockProducts() {
        _products.value = listOf(
            Product(1, "Smartphone", "Latest Android smartphone with 5G", 699.99, "https://via.placeholder.com/300x200?text=Smartphone", "Electronics"),
            Product(2, "Laptop", "High-performance laptop for work and gaming", 1299.99, "https://via.placeholder.com/300x200?text=Laptop", "Electronics"),
            Product(3, "Headphones", "Noise-cancelling wireless headphones", 299.99, "https://via.placeholder.com/300x200?text=Headphones", "Electronics"),
            Product(4, "Smart Watch", "Fitness tracker and smartwatch", 399.99, "https://via.placeholder.com/300x200?text=Smart+Watch", "Electronics"),
            Product(5, "Tablet", "10-inch tablet with stylus", 599.99, "https://via.placeholder.com/300x200?text=Tablet", "Electronics"),
            Product(6, "Camera", "Professional DSLR camera", 1899.99, "https://via.placeholder.com/300x200?text=Camera", "Electronics"),
            Product(7, "Speaker", "Portable Bluetooth speaker", 149.99, "https://via.placeholder.com/300x200?text=Speaker", "Electronics"),
            Product(8, "Gaming Console", "Next-gen gaming console", 499.99, "https://via.placeholder.com/300x200?text=Gaming+Console", "Electronics")
        )
    }

    suspend fun login(email: String, password: String): Result<User> {
        // Mock login - replace with API call
        delay(1000)
        return if (email.isNotEmpty() && password.isNotEmpty()) {
            val user = User(
                id = 1,
                name = "John Doe",
                email = email,
                phone = "+1234567890",
                address = "123 Main St, City, Country"
            )
            _currentUser.value = user
            loadMockOrders()
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    fun logout() {
        _currentUser.value = null
        _cartItems.value = emptyList()
        _wishlist.value = emptyList()
        _orders.value = emptyList()
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentCart.add(CartItem(product, quantity))
        }
        _cartItems.value = currentCart
    }

    fun removeFromCart(productId: Int) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun updateCartItemQuantity(productId: Int, quantity: Int) {
        val currentCart = _cartItems.value.toMutableList()
        currentCart.find { it.product.id == productId }?.quantity = quantity
        _cartItems.value = currentCart
    }

    fun addToWishlist(product: Product) {
        if (!_wishlist.value.any { it.id == product.id }) {
            _wishlist.value = _wishlist.value + product
        }
    }

    fun removeFromWishlist(productId: Int) {
        _wishlist.value = _wishlist.value.filter { it.id != productId }
    }

    fun isInWishlist(productId: Int): Boolean {
        return _wishlist.value.any { it.id == productId }
    }

    private fun loadMockOrders() {
        _orders.value = listOf(
            Order(
                id = "ORD001",
                date = "2024-01-15",
                total = 1299.99,
                status = OrderStatus.DELIVERED,
                items = listOf(
                    CartItem(Product(2, "Laptop", "High-performance laptop", 1299.99, "", "Electronics"), 1)
                )
            ),
            Order(
                id = "ORD002",
                date = "2024-01-20",
                total = 699.99,
                status = OrderStatus.SHIPPED,
                items = listOf(
                    CartItem(Product(1, "Smartphone", "Latest Android smartphone", 699.99, "", "Electronics"), 1)
                )
            )
        )
    }

    fun getCartTotal(): Double {
        return _cartItems.value.sumOf { it.product.price * it.quantity }
    }
}