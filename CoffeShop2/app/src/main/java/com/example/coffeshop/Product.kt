package com.example.coffeshop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val imageRes: Int,
    val name: String,
    val price: String,
    val category: String,
    val description: String // <-- Add the new description property
) : Parcelable