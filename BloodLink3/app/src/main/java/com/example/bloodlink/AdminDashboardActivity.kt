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
import com.example.bloodlink.data.BloodRequest
import com.example.bloodlink.viewmodel.AdminDashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardActivity : AppCompatActivity() {

    private val viewModel: AdminDashboardViewModel by viewModels()
    private lateinit var pendingRequestsRecyclerView: RecyclerView
    private lateinit var requestAdapter: AdminRequestAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var welcomeMessage: TextView
    private lateinit var logoutButton: Button
    private lateinit var totalUsersCount: TextView
    private lateinit var totalRequestsCount: TextView
    private lateinit var pendingRequestsCount: TextView
    private lateinit var emergencyRequestsCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        pendingRequestsRecyclerView = findViewById(R.id.pending_requests_recyclerview)
        welcomeMessage = findViewById(R.id.welcome_message)
        logoutButton = findViewById(R.id.logout_button)
        totalUsersCount = findViewById(R.id.total_users_count)
        totalRequestsCount = findViewById(R.id.total_requests_count)
        pendingRequestsCount = findViewById(R.id.pending_requests_count)
        emergencyRequestsCount = findViewById(R.id.emergency_requests_count)

        setupRecyclerView()
        observeViewModel()
        fetchDashboardData()

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerView() {
        requestAdapter = AdminRequestAdapter(emptyList(),
            { request -> viewModel.approveRequest(request) },
            { request -> viewModel.rejectRequest(request) }
        )
        pendingRequestsRecyclerView.adapter = requestAdapter
        pendingRequestsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.pendingRequests.observe(this) { requests ->
            requestAdapter.updateRequests(requests)
            pendingRequestsCount.text = requests.size.toString()
        }

        viewModel.allUsers.observe(this) { users ->
            totalUsersCount.text = users.size.toString()
        }

        viewModel.allRequests.observe(this) { requests ->
            totalRequestsCount.text = requests.size.toString()
            val emergencyCount = requests.count { it.isEmergency }
            emergencyRequestsCount.text = emergencyCount.toString()
        }
    }

    private fun fetchDashboardData() {
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