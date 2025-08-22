package com.example.coffeshop

object CartManager {
    val cartItems = mutableListOf<Pair<String, String>>() // Pair<name, price>

    fun addItem(name: String, price: String) {
        cartItems.add(Pair(name, price))
    }

    fun getItems(): List<Pair<String, String>> = cartItems

    fun getTotal(): Double {
        return cartItems.sumOf { it.second.replace("$", "").toDoubleOrNull() ?: 0.0 }
    }
}
