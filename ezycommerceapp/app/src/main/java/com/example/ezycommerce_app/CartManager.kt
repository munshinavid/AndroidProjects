package com.example.ezycommerce_app

object CartManager {
    private val _cartItems = mutableListOf<Product>()  // private mutable list
    val cartItems: List<Product> get() = _cartItems       // public read-only list

    fun addToCart(product: Product) {
        _cartItems.add(product)
    }

    fun removeFromCart(product: Product) {
        _cartItems.remove(product)
    }

    fun clearCart() {
        _cartItems.clear()
    }
    // ✅ Add this function
    fun getCartCount(): Int {
        return _cartItems.size
    }
}