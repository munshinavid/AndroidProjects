package com.example.ecommapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val orderDate: Long,
    val totalAmount: Double,
    val status: String,
    val addressId: Int
)