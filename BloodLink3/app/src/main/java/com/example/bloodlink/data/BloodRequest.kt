package com.example.bloodlink.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class BloodRequest(
    val requesterName: String = "",
    val bloodGroup: String = "",
    val city: String = "",
    val hospital: String = "",
    val contactNumber: String = "",
    val urgency: String = "",
    val createdBy: String = "",
    val status: String = "pending",
    val timestamp: Timestamp = Timestamp.now()
) {
    @get:Exclude
    val isEmergency: Boolean
        get() = urgency.equals("emergency", ignoreCase = true)
}