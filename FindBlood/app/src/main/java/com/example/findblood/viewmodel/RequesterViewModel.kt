package com.example.findblood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findblood.data.model.Request
import com.example.findblood.data.model.User
import com.example.findblood.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.UUID

class RequesterViewModel : ViewModel() {

    private val _requestCreated = MutableLiveData<Boolean>()
    val requestCreated: LiveData<Boolean> = _requestCreated

    private val _myRequests = MutableLiveData<List<Request>>()
    val myRequests: LiveData<List<Request>> = _myRequests

    private val _searchedDonors = MutableLiveData<List<User>>()
    val searchedDonors: LiveData<List<User>> = _searchedDonors

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun createRequest(
        bloodGroup: String,
        city: String,
        hospitalName: String,
        contactNumber: String,
        isEmergency: Boolean
    ) {
        val userId = auth.currentUser?.uid ?: return
        val requestId = UUID.randomUUID().toString()
        val request = Request(
            requestId = requestId,
            bloodGroup = bloodGroup,
            city = city,
            hospitalName = hospitalName,
            contactNumber = contactNumber,
            urgency = if (isEmergency) "emergency" else "normal",
            status = "pending",
            createdBy = userId,
            timestamp = com.google.firebase.Timestamp.now()
        )

        firestore.collection(FirebaseUtils.REQUESTS).document(requestId).set(request)
            .addOnSuccessListener {
                _requestCreated.value = true
            }
            .addOnFailureListener {
                _requestCreated.value = false
            }
    }

    fun fetchMyRequests() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection(FirebaseUtils.REQUESTS)
            .whereEqualTo("createdBy", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                _myRequests.value = snapshots?.toObjects(Request::class.java)
            }
    }

    fun searchDonors(bloodGroup: String, city: String) {
        firestore.collection(FirebaseUtils.USERS)
            .whereEqualTo("role", "donor")
            .whereEqualTo("isAvailable", true)
            .whereEqualTo("bloodGroup", bloodGroup)
            .whereEqualTo("city", city)
            .get()
            .addOnSuccessListener {
                _searchedDonors.value = it.toObjects(User::class.java)
            }
    }
}