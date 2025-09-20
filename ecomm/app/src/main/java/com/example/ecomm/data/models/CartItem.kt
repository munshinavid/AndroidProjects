// File: data/models/CartItem.kt
package com.example.ecomm.data.models

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)