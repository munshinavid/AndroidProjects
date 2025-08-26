package com.example.ezycommerce_app

data class Product(
    val name: String,
    val price: Double,
    val description: String,
    val imageResId: Int,
    val category: String,
    var quantity: Int = 1 // default quantity is 1
)


