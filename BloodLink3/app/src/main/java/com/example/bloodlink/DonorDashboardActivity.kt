package com.example.bloodlink

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.adapter.BloodRequestAdapter
import com.example.bloodlink.viewmodel.DonorDashboardViewModel

class DonorDashboardActivity : AppCompatActivity() {

    private val viewModel: DonorDashboardViewModel by viewModels()
    private lateinit var requestsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: BloodRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_dashboard)

        requestsRecyclerView = findViewById(R.id.requests_recyclerview)
        progressBar = findViewById(R.id.progress_bar)

        setupRecyclerView()
        observeViewModel()
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
}