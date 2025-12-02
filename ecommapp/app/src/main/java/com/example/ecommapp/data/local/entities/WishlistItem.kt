package com.example.ecommapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val productImage: String
)