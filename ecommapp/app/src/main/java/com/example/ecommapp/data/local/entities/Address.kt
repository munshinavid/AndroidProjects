package com.example.ecommapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val fullName: String,
    val phone: String,
    val addressLine: String,
    val city: String,
    val state: String,
    val pincode: String,
    val isDefault: Boolean = false
)