package com.example.bloodlink.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bloodlink.data.BloodRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RequesterDashboardViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _myRequests = MutableLiveData<List<BloodRequest>>()
    val myRequests: LiveData<List<BloodRequest>> = _myRequests

    init {
        fetchMyRequests()
    }

    private fun fetchMyRequests() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("requests")
            .whereEqualTo("createdBy", userId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val requestList = snapshots.toObjects(BloodRequest::class.java)
                    _myRequests.value = requestList
                }
            }
    }
}