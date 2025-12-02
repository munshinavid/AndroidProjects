package com.example.ecommapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommapp.EcommApp
import com.example.ecommapp.data.local.entities.CartItem
import com.example.ecommapp.data.local.entities.Product
import com.example.ecommapp.data.repository.EcommRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EcommRepository((application as EcommApp).database)

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    fun loadCartItems(userId: Int) {
        viewModelScope.launch {
            repository.getCartItems(userId).collect { items ->
                _cartItems.value = items
                calculateTotal(items)
            }
        }
    }

    private fun calculateTotal(items: List<CartItem>) {
        _totalAmount.value = items.sumOf { it.productPrice * it.quantity }
    }

    fun addToCart(userId: Int, product: Product) {
        viewModelScope.launch {
            val existing = repository.getCartItem(userId, product.id)
            if (existing != null) {
                repository.updateCartItem(existing.copy(quantity = existing.quantity + 1))
            } else {
                repository.addToCart(
                    CartItem(
                        userId = userId,
                        productId = product.id,
                        quantity = 1,
                        productName = product.name,
                        productPrice = product.price * (1 - product.discount / 100.0),
                        productImage = product.imageRes
                    )
                )
            }
        }
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                repository.updateCartItem(cartItem.copy(quantity = newQuantity))
            } else {
                repository.removeFromCart(cartItem)
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            repository.removeFromCart(cartItem)
        }
    }

    fun clearCart(userId: Int) {
        viewModelScope.launch {
            repository.clearCart(userId)
        }
    }
}