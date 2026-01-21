package com.example.bloodlink.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bloodlink.data.BloodRequest
import com.example.bloodlink.model.User
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _allRequests = MutableLiveData<List<BloodRequest>>()
    val allRequests: LiveData<List<BloodRequest>> = _allRequests

    private val _pendingRequests = MutableLiveData<List<BloodRequest>>()
    val pendingRequests: LiveData<List<BloodRequest>> = _pendingRequests

    private val _allUsers = MutableLiveData<List<User>>()
    val allUsers: LiveData<List<User>> = _allUsers

    init {
        fetchAllRequests()
        fetchAllUsers()
    }

    private fun fetchAllRequests() {
        db.collection("requests")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val requestList = snapshots.toObjects(BloodRequest::class.java)
                    _allRequests.value = requestList
                    _pendingRequests.value = requestList.filter { it.status == "pending" }
                }
            }
    }

    private fun fetchAllUsers() {
        db.collection("users")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val userList = snapshots.toObjects(User::class.java)
                    _allUsers.value = userList
                }
            }
    }

    fun approveRequest(request: BloodRequest) {
        getRequestIdAndUpdateRequest(request, "approved")
    }

    fun rejectRequest(request: BloodRequest) {
        getRequestIdAndUpdateRequest(request, "rejected")
    }

    private fun getRequestIdAndUpdateRequest(request: BloodRequest, status: String) {
        db.collection("requests")
            .whereEqualTo("createdBy", request.createdBy)
            .whereEqualTo("timestamp", request.timestamp)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val documentId = documents.first().id
                    updateRequestStatus(documentId, status)
                }
            }
    }

    private fun updateRequestStatus(requestId: String, status: String) {
        db.collection("requests").document(requestId)
            .update("status", status)
    }
}