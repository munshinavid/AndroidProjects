package com.example.findblood.data.model

import com.google.firebase.Timestamp

data class Donation(
    val donationId: String = "",
    val donorId: String = "",
    val bloodGroup: String = "",
    val hospitalName: String = "",
    val city: String = "",
    val donationDate: Timestamp? = null
)