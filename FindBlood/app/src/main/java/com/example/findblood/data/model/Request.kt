package com.example.findblood.data.model

import com.google.firebase.Timestamp

data class Request(
    val requestId: String = "",
    val bloodGroup: String = "",
    val city: String = "",
    val hospitalName: String = "",
    val contactNumber: String = "",
    val urgency: String = "",
    val status: String = "",
    val createdBy: String = "",
    val timestamp: Timestamp? = null
)