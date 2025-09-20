// File: data/models/Order.kt
package com.example.ecomm.data.models

data class Order(
    val id: String,
    val date: String,
    val total: Double,
    val status: OrderStatus,
    val items: List<CartItem>
)