package com.example.ecommapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageRes: String,
    val rating: Float,
    val reviewCount: Int,
    val discount: Int = 0,
    val stock: Int = 100
)