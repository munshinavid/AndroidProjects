package com.example.findblood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findblood.data.model.User
import com.example.findblood.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _userRole = MutableLiveData<String>()
    val userRole: LiveData<String> = _userRole

    fun registerUser(
        name: String,
        email: String,
        pass: String,
        role: String
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = it.result?.user
                    val user = User(
                        uid = firebaseUser?.uid ?: "",
                        name = name,
                        email = email,
                        role = role.lowercase()
                    )
                    FirebaseFirestore.getInstance().collection(FirebaseUtils.USERS)
                        .document(firebaseUser?.uid ?: "").set(user)
                        .addOnSuccessListener {
                            _registrationResult.value = true
                        }.addOnFailureListener { e ->
                            _registrationResult.value = false
                        }
                } else {
                    _registrationResult.value = false
                }
            }
    }

    fun loginUser(email: String, pass: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = true
                    fetchUserRole(task.result?.user?.uid ?: "")
                } else {
                    _loginResult.value = false
                }
            }
    }

    private fun fetchUserRole(uid: String) {
        FirebaseFirestore.getInstance().collection(FirebaseUtils.USERS).document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    _userRole.value = user?.role ?: ""
                } else {
                    _userRole.value = ""
                }
            }
            .addOnFailureListener { exception ->
                _userRole.value = ""
            }
    }
}