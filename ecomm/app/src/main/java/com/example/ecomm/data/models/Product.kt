// File: data/models/Product.kt
package com.example.ecomm.data.models

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val rating: Float = 4.5f,
    val inStock: Boolean = true
)