package com.example.bloodlink

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.adapter.AdminRequestAdapter
import com.example.bloodlink.viewmodel.AdminDashboardViewModel

class AdminDashboardActivity : AppCompatActivity() {

    private val viewModel: AdminDashboardViewModel by viewModels()
    private lateinit var allRequestsRecyclerView: RecyclerView
    private lateinit var adapter: AdminRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        allRequestsRecyclerView = findViewById(R.id.all_requests_recyclerview)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = AdminRequestAdapter(emptyList(),
            { request -> viewModel.approveRequest(request) },
            { request -> viewModel.rejectRequest(request) }
        )
        allRequestsRecyclerView.adapter = adapter
        allRequestsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.allRequests.observe(this) { requests ->
            adapter.updateRequests(requests)
        }
    }
}