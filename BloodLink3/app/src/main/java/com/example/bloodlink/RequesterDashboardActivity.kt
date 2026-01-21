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
import com.example.bloodlink.adapter.MyRequestAdapter
import com.example.bloodlink.viewmodel.RequesterDashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RequesterDashboardActivity : AppCompatActivity() {

    private val viewModel: RequesterDashboardViewModel by viewModels()
    private lateinit var myRequestsRecyclerView: RecyclerView
    private lateinit var adapter: MyRequestAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var welcomeMessage: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requester_dashboard)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val createRequestButton = findViewById<Button>(R.id.create_request_button)
        myRequestsRecyclerView = findViewById(R.id.my_requests_recyclerview)
        welcomeMessage = findViewById(R.id.welcome_message)
        logoutButton = findViewById(R.id.logout_button)

        setupRecyclerView()
        observeViewModel()
        fetchRequesterData()

        createRequestButton.setOnClickListener {
            val intent = Intent(this, CreateRequestActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = MyRequestAdapter(emptyList())
        myRequestsRecyclerView.adapter = adapter
        myRequestsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.myRequests.observe(this) { requests ->
            adapter.updateRequests(requests)
        }
    }

    private fun fetchRequesterData() {
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