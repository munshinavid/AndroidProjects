package com.example.bloodlink.model

import java.util.Date

data class User(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val bloodGroup: String = "",
    val city: String = "",
    val role: String = "",
    val isAvailable: Boolean = true,
    val lastDonationDate: Date = Date()
)
