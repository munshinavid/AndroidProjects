// File: viewmodel/MainViewModel.kt
package com.example.ecomm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomm.data.models.Product
import com.example.ecomm.data.repository.EcommerceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: EcommerceRepository
) : ViewModel() {

    val products = repository.products
    val cartItems = repository.cartItems
    val wishlist = repository.wishlist
    val currentUser = repository.currentUser
    val orders = repository.orders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.login(email, password)
            _isLoading.value = false
            onResult(result.isSuccess)
        }
    }

    fun logout() {
        repository.logout()
    }

    fun addToCart(product: Product) {
        repository.addToCart(product)
    }

    fun removeFromCart(productId: Int) {
        repository.removeFromCart(productId)
    }

    fun updateCartQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
        } else {
            repository.updateCartItemQuantity(productId, quantity)
        }
    }

    fun toggleWishlist(product: Product) {
        if (repository.isInWishlist(product.id)) {
            repository.removeFromWishlist(product.id)
        } else {
            repository.addToWishlist(product)
        }
    }

    fun isInWishlist(productId: Int): Boolean {
        return repository.isInWishlist(productId)
    }

    fun getCartTotal(): Double {
        return repository.getCartTotal()
    }
}