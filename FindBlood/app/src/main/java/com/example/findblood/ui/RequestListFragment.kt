package com.example.findblood.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findblood.databinding.FragmentRequestListBinding
import com.example.findblood.viewmodel.AdminViewModel

class RequestListFragment : Fragment() {

    private var _binding: FragmentRequestListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AdminViewModel
    private lateinit var requestAdapter: RequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminViewModel::class.java)

        setupRecyclerView()

        viewModel.requests.observe(viewLifecycleOwner) {
            requestAdapter.updateRequests(it)
        }

        viewModel.fetchAllRequests()
    }

    private fun setupRecyclerView() {
        requestAdapter = RequestAdapter(emptyList(), false, // isDonor is false for admin
            onApprove = { request ->
                viewModel.updateRequestStatus(request, "approved")
            },
            onReject = { request ->
                viewModel.updateRequestStatus(request, "rejected")
            }
        )
        binding.requestsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}