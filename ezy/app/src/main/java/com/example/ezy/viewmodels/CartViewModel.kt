package com.example.ezy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezy.*
import com.example.ezy.utils.PrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val repository = EzyCommerceRepository()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _cartTotal = MutableStateFlow("0")
    val cartTotal: StateFlow<String> = _cartTotal

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init { loadCart() }

    fun loadCart() {
        viewModelScope.launch {
            val userId = PrefsManager.userId.first() ?: return@launch
            repository.getCart(userId).fold(
                onSuccess = { response ->
                    _cartItems.value = response.cartItems ?: emptyList()
                    _cartTotal.value = response.totalCost ?: "0"
                },
                onFailure = { }
            )
        }
    }

    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            val userId = PrefsManager.userId.first() ?: return@launch
            _isLoading.value = true
            repository.addToCart(productId, quantity, userId).fold(
                onSuccess = { loadCart() },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }

    fun updateQuantity(cartItemId: Int, quantity: Int) {
        viewModelScope.launch {
            val userId = PrefsManager.userId.first() ?: return@launch
            repository.updateCartQuantity(cartItemId, quantity, userId).fold(
                onSuccess = { loadCart() },
                onFailure = { }
            )
        }
    }

    fun removeFromCart(cartItemId: Int) {
        viewModelScope.launch {
            val userId = PrefsManager.userId.first() ?: return@launch
            repository.removeFromCart(cartItemId, userId).fold(
                onSuccess = { loadCart() },
                onFailure = { }
            )
        }
    }

    fun placeOrder(paymentMethod: String, fullName: String, address: String, phone: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val userId = PrefsManager.userId.first() ?: return@launch
            _isLoading.value = true
            val customerDetails = CustomerDetails(fullName, address, phone)
            repository.placeOrder(paymentMethod, customerDetails, userId).fold(
                onSuccess = {
                    loadCart()
                    onSuccess()
                },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }
}