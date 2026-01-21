package com.example.bloodlink.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bloodlink.data.BloodRequest
import com.google.firebase.firestore.FirebaseFirestore

class DonorDashboardViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _requests = MutableLiveData<List<BloodRequest>>()
    val requests: LiveData<List<BloodRequest>> = _requests

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchApprovedRequests()
    }

    private fun fetchApprovedRequests() {
        _isLoading.value = true
        db.collection("requests")
            .whereEqualTo("status", "approved")
            .addSnapshotListener { snapshots, e ->
                _isLoading.value = false
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val requestList = snapshots.toObjects(BloodRequest::class.java)
                    _requests.value = requestList
                }
            }
    }
}