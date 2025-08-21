package com.example.ezycommerce_app

data class Product(
    val name: String,
    val price: Double,
    val description: String,
    val imageResId: Int // image from drawable
)
