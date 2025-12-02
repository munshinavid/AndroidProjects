package com.example.ecommapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommapp.EcommApp
import com.example.ecommapp.data.local.entities.CartItem
import com.example.ecommapp.data.local.entities.Order
import com.example.ecommapp.data.local.entities.OrderItem
import com.example.ecommapp.data.repository.EcommRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EcommRepository((application as EcommApp).database)

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _orderItems = MutableStateFlow<Map<Int, List<OrderItem>>>(emptyMap())
    val orderItems: StateFlow<Map<Int, List<OrderItem>>> = _orderItems.asStateFlow()

    fun loadOrders(userId: Int) {
        viewModelScope.launch {
            repository.getUserOrders(userId).collect { orders ->
                _orders.value = orders
                orders.forEach { order ->
                    loadOrderItems(order.id)
                }
            }
        }
    }

    private fun loadOrderItems(orderId: Int) {
        viewModelScope.launch {
            repository.getOrderItems(orderId).collect { items ->
                _orderItems.value = _orderItems.value + (orderId to items)
            }
        }
    }

    fun placeOrder(
        userId: Int,
        cartItems: List<CartItem>,
        totalAmount: Double,
        addressId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val order = Order(
                    userId = userId,
                    orderDate = System.currentTimeMillis(),
                    totalAmount = totalAmount,
                    status = "Pending",
                    addressId = addressId
                )

                val orderId = repository.createOrder(order)

                val orderItems = cartItems.map { cartItem ->
                    OrderItem(
                        orderId = orderId.toInt(),
                        productId = cartItem.productId,
                        productName = cartItem.productName,
                        productPrice = cartItem.productPrice,
                        quantity = cartItem.quantity,
                        productImage = cartItem.productImage
                    )
                }

                repository.createOrderItems(orderItems)
                repository.clearCart(userId)

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to place order")
            }
        }
    }
}