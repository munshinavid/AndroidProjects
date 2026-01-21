package com.example.bloodlink

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.adapter.BloodRequestAdapter
import com.example.bloodlink.viewmodel.DonorDashboardViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DonorDashboardActivity : AppCompatActivity() {

    private val viewModel: DonorDashboardViewModel by viewModels()
    private lateinit var requestsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: BloodRequestAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var welcomeMessage: TextView
    private lateinit var availabilitySwitch: SwitchMaterial
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_dashboard)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        requestsRecyclerView = findViewById(R.id.requests_recyclerview)
        progressBar = findViewById(R.id.progress_bar)
        welcomeMessage = findViewById(R.id.welcome_message)
        availabilitySwitch = findViewById(R.id.availability_switch)
        logoutButton = findViewById(R.id.logout_button)

        setupRecyclerView()
        observeViewModel()
        fetchDonorData()

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        availabilitySwitch.setOnCheckedChangeListener { _, isChecked ->
            updateAvailability(isChecked)
        }
    }

    private fun setupRecyclerView() {
        adapter = BloodRequestAdapter(emptyList())
        requestsRecyclerView.adapter = adapter
        requestsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.requests.observe(this) { requests ->
            adapter.updateRequests(requests)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun fetchDonorData() {
        val userId = auth.currentUser!!.uid
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name")
                    val isAvailable = document.getBoolean("isAvailable")
                    welcomeMessage.text = "Welcome, $name!"
                    availabilitySwitch.isChecked = isAvailable ?: true
                } else {
                    Toast.makeText(baseContext, "User data not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(baseContext, "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateAvailability(isAvailable: Boolean) {
        val userId = auth.currentUser!!.uid
        db.collection("users").document(userId)
            .update("isAvailable", isAvailable)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Availability updated.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(baseContext, "Error updating availability: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}