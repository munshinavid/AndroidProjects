package com.example.bloodlink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.adapter.MyRequestAdapter
import com.example.bloodlink.viewmodel.RequesterDashboardViewModel

class RequesterDashboardActivity : AppCompatActivity() {

    private val viewModel: RequesterDashboardViewModel by viewModels()
    private lateinit var myRequestsRecyclerView: RecyclerView
    private lateinit var adapter: MyRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requester_dashboard)

        val createRequestButton = findViewById<Button>(R.id.create_request_button)
        myRequestsRecyclerView = findViewById(R.id.my_requests_recyclerview)

        setupRecyclerView()
        observeViewModel()

        createRequestButton.setOnClickListener {
            val intent = Intent(this, CreateRequestActivity::class.java)
            startActivity(intent)
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
}