package com.example.ezycommerce_app

object CartManager {
    private val _cartItems = mutableListOf<Product>()  // private mutable list
    val cartItems: List<Product> get() = _cartItems    // public read-only list

    fun addToCart(product: Product) {
        val existingProduct = _cartItems.find { it.name == product.name && it.category == product.category }
        if (existingProduct != null) {
            existingProduct.quantity++
        } else {
            product.quantity = 1
            _cartItems.add(product)
        }
    }

    fun removeFromCart(product: Product) {
        val existingProduct = _cartItems.find { it.name == product.name && it.category == product.category }
        if (existingProduct != null) {
            if (existingProduct.quantity > 1) {
                existingProduct.quantity--
            } else {
                _cartItems.remove(existingProduct)
            }
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun getCartCount(): Int {
        // total number of items (sum of all quantities)
        return _cartItems.sumOf { it.quantity }
    }

    fun getTotal(): Double {
        // total price of all items in cart
        return _cartItems.sumOf { it.price * it.quantity }
    }
    //this is test
}
