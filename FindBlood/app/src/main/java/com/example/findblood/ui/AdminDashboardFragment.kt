package com.example.findblood.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findblood.R
import com.example.findblood.databinding.FragmentAdminDashboardBinding
import com.example.findblood.viewmodel.AdminViewModel

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AdminViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminViewModel::class.java)

        setupRecyclerView()

        binding.btnViewRequests.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RequestListFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.users.observe(viewLifecycleOwner) {
            userAdapter.updateUsers(it)
        }

        viewModel.totalDonors.observe(viewLifecycleOwner) {
            binding.tvTotalDonors.text = "Total Donors: $it"
        }

        viewModel.availableDonors.observe(viewLifecycleOwner) {
            binding.tvAvailableDonors.text = "Available Donors: $it"
        }

        viewModel.totalRequests.observe(viewLifecycleOwner) {
            binding.tvTotalRequests.text = "Total Requests: $it"
        }

        viewModel.emergencyRequests.observe(viewLifecycleOwner) {
            binding.tvEmergencyRequests.text = "Emergency Requests: $it"
        }

        viewModel.fetchAllUsers()
        viewModel.fetchAnalytics()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList())
        binding.usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}