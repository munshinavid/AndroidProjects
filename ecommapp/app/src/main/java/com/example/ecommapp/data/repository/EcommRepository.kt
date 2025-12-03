package com.example.ecommapp.data.repository

import com.example.ecommapp.data.local.AppDatabase
import com.example.ecommapp.data.local.entities.*
import kotlinx.coroutines.flow.Flow

class EcommRepository(private val database: AppDatabase) {

    // User operations
    suspend fun login(email: String, password: String) = database.userDao().login(email, password)
    fun getUserById(userId: Int) = database.userDao().getUserById(userId)
    suspend fun updateUser(user: User) = database.userDao().update(user)

    // Product operations
    fun getAllProducts() = database.productDao().getAllProducts()
    fun getProductById(productId: Int) = database.productDao().getProductById(productId)
    fun getProductsByCategory(category: String) = database.productDao().getProductsByCategory(category)
    fun searchProducts(query: String) = database.productDao().searchProducts(query)
    fun getAllCategories() = database.productDao().getAllCategories()

    // Cart operations
    fun getCartItems(userId: Int) = database.cartDao().getCartItems(userId)
    suspend fun addToCart(cartItem: CartItem) = database.cartDao().insert(cartItem)
    suspend fun updateCartItem(cartItem: CartItem) = database.cartDao().update(cartItem)
    suspend fun removeFromCart(cartItem: CartItem) = database.cartDao().delete(cartItem)
    suspend fun clearCart(userId: Int) = database.cartDao().clearCart(userId)
    suspend fun getCartItem(userId: Int, productId: Int) = database.cartDao().getCartItem(userId, productId)

    // Wishlist operations
    fun getWishlistItems(userId: Int) = database.wishlistDao().getWishlistItems(userId)
    suspend fun addToWishlist(wishlistItem: WishlistItem) = database.wishlistDao().insert(wishlistItem)
    suspend fun removeFromWishlist(wishlistItem: WishlistItem) = database.wishlistDao().delete(wishlistItem)
    suspend fun getWishlistItem(userId: Int, productId: Int) = database.wishlistDao().getWishlistItem(userId, productId)

    // Order operations
    suspend fun createOrder(order: Order) = database.orderDao().insertOrder(order)
    suspend fun createOrderItems(orderItems: List<OrderItem>) = database.orderDao().insertOrderItems(orderItems)
    fun getUserOrders(userId: Int) = database.orderDao().getUserOrders(userId)
    fun getOrderItems(orderId: Int) = database.orderDao().getOrderItems(orderId)

    // Address operations
    fun getUserAddresses(userId: Int) = database.addressDao().getUserAddresses(userId)
    suspend fun addAddress(address: Address) = database.addressDao().insert(address)
    suspend fun updateAddress(address: Address) = database.addressDao().update(address)
    suspend fun deleteAddress(address: Address) = database.addressDao().delete(address)
    suspend fun clearDefaultAddress(userId: Int) = database.addressDao().clearDefaultAddress(userId)
}