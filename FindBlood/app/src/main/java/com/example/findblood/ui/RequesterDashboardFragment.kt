package com.example.findblood.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findblood.R
import com.example.findblood.databinding.FragmentRequesterDashboardBinding
import com.example.findblood.viewmodel.RequesterViewModel

class RequesterDashboardFragment : Fragment() {

    private var _binding: FragmentRequesterDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RequesterViewModel
    private lateinit var requestAdapter: RequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequesterDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequesterViewModel::class.java)

        setupRecyclerView()

        binding.btnCreateRequest.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateRequestFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSearchDonors.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DonorSearchFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.myRequests.observe(viewLifecycleOwner) {
            requestAdapter.updateRequests(it)
        }

        viewModel.fetchMyRequests()
    }

    private fun setupRecyclerView() {
        requestAdapter = RequestAdapter(emptyList(), isDonor = false, onApprove = {}, onReject = {})
        binding.myRequestsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}