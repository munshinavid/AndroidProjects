package com.example.ecommapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommapp.EcommApp
import com.example.ecommapp.data.local.entities.Product
import com.example.ecommapp.data.local.entities.WishlistItem
import com.example.ecommapp.data.repository.EcommRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EcommRepository((application as EcommApp).database)

    private val _wishlistItems = MutableStateFlow<List<WishlistItem>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItem>> = _wishlistItems.asStateFlow()

    private val _wishlistProductIds = MutableStateFlow<Set<Int>>(emptySet())
    val wishlistProductIds: StateFlow<Set<Int>> = _wishlistProductIds.asStateFlow()

    fun loadWishlist(userId: Int) {
        viewModelScope.launch {
            repository.getWishlistItems(userId).collect { items ->
                _wishlistItems.value = items
                _wishlistProductIds.value = items.map { it.productId }.toSet()
            }
        }
    }

    fun toggleWishlist(userId: Int, product: Product) {
        viewModelScope.launch {
            val existing = repository.getWishlistItem(userId, product.id)
            if (existing != null) {
                repository.removeFromWishlist(existing)
            } else {
                repository.addToWishlist(
                    WishlistItem(
                        userId = userId,
                        productId = product.id,
                        productName = product.name,
                        productPrice = product.price,
                        productImage = product.imageRes
                    )
                )
            }
        }
    }

    fun removeFromWishlist(wishlistItem: WishlistItem) {
        viewModelScope.launch {
            repository.removeFromWishlist(wishlistItem)
        }
    }

    fun isInWishlist(productId: Int): Boolean {
        return _wishlistProductIds.value.contains(productId)
    }
}