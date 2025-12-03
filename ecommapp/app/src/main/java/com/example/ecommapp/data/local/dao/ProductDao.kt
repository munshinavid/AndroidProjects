package com.example.ecommapp.data.local.dao

import androidx.room.*
import com.example.ecommapp.data.local.entities.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: Int): Flow<Product?>

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Query("SELECT DISTINCT category FROM products")
    fun getAllCategories(): Flow<List<String>>
}