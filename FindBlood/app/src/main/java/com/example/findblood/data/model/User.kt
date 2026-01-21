package com.example.findblood.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "",
    val bloodGroup: String = "",
    val city: String = "",
    val isAvailable: Boolean = false,
    val lastDonationDate: Timestamp? = null
)