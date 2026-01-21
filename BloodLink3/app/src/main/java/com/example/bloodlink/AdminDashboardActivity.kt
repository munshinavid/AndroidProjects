package com.example.bloodlink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.adapter.AdminRequestAdapter
import com.example.bloodlink.adapter.UserAdapter
import com.example.bloodlink.viewmodel.AdminDashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardActivity : AppCompatActivity() {

    private val viewModel: AdminDashboardViewModel by viewModels()
    private lateinit var allRequestsRecyclerView: RecyclerView
    private lateinit var allUsersRecyclerView: RecyclerView
    private lateinit var requestAdapter: AdminRequestAdapter
    private lateinit var userAdapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var welcomeMessage: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        allRequestsRecyclerView = findViewById(R.id.all_requests_recyclerview)
        allUsersRecyclerView = findViewById(R.id.all_users_recyclerview)
        welcomeMessage = findViewById(R.id.welcome_message)
        logoutButton = findViewById(R.id.logout_button)

        setupRecyclerViews()
        observeViewModel()
        fetchAdminData()

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerViews() {
        requestAdapter = AdminRequestAdapter(emptyList(),
            { request -> viewModel.approveRequest(request) },
            { request -> viewModel.rejectRequest(request) }
        )
        allRequestsRecyclerView.adapter = requestAdapter
        allRequestsRecyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(emptyList())
        allUsersRecyclerView.adapter = userAdapter
        allUsersRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.allRequests.observe(this) { requests ->
            requestAdapter.updateRequests(requests)
        }
        viewModel.allUsers.observe(this) { users ->
            userAdapter.updateUsers(users)
        }
    }

    private fun fetchAdminData() {
        val userId = auth.currentUser!!.uid
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name")
                    welcomeMessage.text = "Welcome, $name!"
                } else {
                    Toast.makeText(baseContext, "User data not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(baseContext, "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}