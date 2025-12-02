package com.example.ecommapp.data.local.dao

import androidx.room.*
import com.example.ecommapp.data.local.entities.WishlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist_items WHERE userId = :userId")
    fun getWishlistItems(userId: Int): Flow<List<WishlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wishlistItem: WishlistItem)

    @Delete
    suspend fun delete(wishlistItem: WishlistItem)

    @Query("SELECT * FROM wishlist_items WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun getWishlistItem(userId: Int, productId: Int): WishlistItem?
}