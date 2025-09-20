// File: data/models/User.kt
package com.example.ecomm.data.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val profileImageUrl: String? = null
)