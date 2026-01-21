package com.example.findblood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findblood.data.model.Request
import com.example.findblood.data.model.User
import com.example.findblood.utils.FirebaseUtils
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _requests = MutableLiveData<List<Request>>()
    val requests: LiveData<List<Request>> = _requests

    private val _totalDonors = MutableLiveData<Int>()
    val totalDonors: LiveData<Int> = _totalDonors

    private val _availableDonors = MutableLiveData<Int>()
    val availableDonors: LiveData<Int> = _availableDonors

    private val _totalRequests = MutableLiveData<Int>()
    val totalRequests: LiveData<Int> = _totalRequests

    private val _emergencyRequests = MutableLiveData<Int>()
    val emergencyRequests: LiveData<Int> = _emergencyRequests

    fun fetchAllUsers() {
        FirebaseFirestore.getInstance().collection(FirebaseUtils.USERS)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val userList = mutableListOf<User>()
                for (doc in snapshots!!) {
                    val user = doc.toObject(User::class.java)
                    userList.add(user)
                }
                _users.value = userList
            }
    }

    fun fetchAllRequests() {
        FirebaseFirestore.getInstance().collection(FirebaseUtils.REQUESTS)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val requestList = snapshots?.toObjects(Request::class.java)
                _requests.value = requestList ?: emptyList()
            }
    }

    fun updateRequestStatus(request: Request, status: String) {
        FirebaseFirestore.getInstance().collection(FirebaseUtils.REQUESTS)
            .document(request.requestId)
            .update("status", status)
    }

    fun fetchAnalytics() {
        // Fetch total donors and available donors
        FirebaseFirestore.getInstance().collection(FirebaseUtils.USERS)
            .whereEqualTo("role", "donor")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val donorList = snapshots?.toObjects(User::class.java)
                _totalDonors.value = donorList?.size ?: 0
                _availableDonors.value = donorList?.count { it.isAvailable } ?: 0
            }

        // Fetch total requests and emergency requests
        FirebaseFirestore.getInstance().collection(FirebaseUtils.REQUESTS)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val requestList = snapshots?.toObjects(Request::class.java)
                _totalRequests.value = requestList?.size ?: 0
                _emergencyRequests.value = requestList?.count { it.urgency == "emergency" } ?: 0
            }
    }
}