package com.example.findblood.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findblood.R
import com.example.findblood.databinding.FragmentDonorDashboardBinding
import com.example.findblood.viewmodel.DonorViewModel

class DonorDashboardFragment : Fragment() {

    private var _binding: FragmentDonorDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DonorViewModel
    private lateinit var requestAdapter: RequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonorDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DonorViewModel::class.java)

        setupRecyclerView()

        binding.btnDonationHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DonationHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.switchAvailability.isChecked = it.isAvailable
            }
        }

        binding.switchAvailability.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateAvailability(isChecked)
        }

        viewModel.approvedRequests.observe(viewLifecycleOwner) { requests ->
            requestAdapter.updateRequests(requests)
        }

        viewModel.fetchCurrentUser()
        viewModel.fetchApprovedRequests()
    }

    private fun setupRecyclerView() {
        requestAdapter = RequestAdapter(emptyList(), isDonor = true, onApprove = {}, onReject = {})
        binding.approvedRequestsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}