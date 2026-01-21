package com.example.findblood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findblood.data.model.Donation
import com.example.findblood.data.model.Request
import com.example.findblood.data.model.User
import com.example.findblood.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DonorViewModel : ViewModel() {

    private val _approvedRequests = MutableLiveData<List<Request>>()
    val approvedRequests: LiveData<List<Request>> = _approvedRequests

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _donationHistory = MutableLiveData<List<Donation>>()
    val donationHistory: LiveData<List<Donation>> = _donationHistory

    private val _profileUpdated = MutableLiveData<Boolean>()
    val profileUpdated: LiveData<Boolean> = _profileUpdated

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchApprovedRequests() {
        firestore.collection(FirebaseUtils.REQUESTS)
            .whereEqualTo("status", "approved")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                _approvedRequests.value = snapshots?.toObjects(Request::class.java)
            }
    }

    fun fetchCurrentUser() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection(FirebaseUtils.USERS).document(userId).get()
            .addOnSuccessListener {
                _user.value = it.toObject(User::class.java)
            }
    }

    fun updateAvailability(isAvailable: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection(FirebaseUtils.USERS).document(userId)
            .update("isAvailable", isAvailable)
    }

    fun fetchDonationHistory() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection(FirebaseUtils.DONATIONS)
            .whereEqualTo("donorId", userId)
            .orderBy("donationDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                _donationHistory.value = snapshots?.toObjects(Donation::class.java)
            }
    }

    fun updateProfile(name: String, phone: String, bloodGroup: String, city: String) {
        val userId = auth.currentUser?.uid ?: return
        val updates = mapOf(
            "name" to name,
            "phone" to phone,
            "bloodGroup" to bloodGroup,
            "city" to city
        )
        firestore.collection(FirebaseUtils.USERS).document(userId).update(updates)
            .addOnSuccessListener {
                _profileUpdated.value = true
            }
            .addOnFailureListener {
                _profileUpdated.value = false
            }
    }
}