package com.example.findblood.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findblood.R
import com.example.findblood.databinding.FragmentDonorSearchBinding
import com.example.findblood.viewmodel.RequesterViewModel

class DonorSearchFragment : Fragment() {

    private var _binding: FragmentDonorSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RequesterViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonorSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequesterViewModel::class.java)

        setupRecyclerView()

        val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, bloodGroups)
        binding.spinnerBloodGroup.adapter = adapter

        binding.btnSearch.setOnClickListener {
            val bloodGroup = binding.spinnerBloodGroup.selectedItem.toString()
            val city = binding.etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                viewModel.searchDonors(bloodGroup, city)
            }
        }

        viewModel.searchedDonors.observe(viewLifecycleOwner) {
            userAdapter.updateUsers(it)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList())
        binding.donorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}