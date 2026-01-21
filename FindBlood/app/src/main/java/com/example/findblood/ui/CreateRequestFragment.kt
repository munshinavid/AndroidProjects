package com.example.findblood.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.findblood.R
import com.example.findblood.databinding.FragmentCreateRequestBinding
import com.example.findblood.viewmodel.RequesterViewModel

class CreateRequestFragment : Fragment() {

    private var _binding: FragmentCreateRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RequesterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequesterViewModel::class.java)

        val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, bloodGroups)
        binding.spinnerBloodGroup.adapter = adapter

        binding.btnSubmitRequest.setOnClickListener {
            val bloodGroup = binding.spinnerBloodGroup.selectedItem.toString()
            val city = binding.etCity.text.toString().trim()
            val hospitalName = binding.etHospitalName.text.toString().trim()
            val contactNumber = binding.etContactNumber.text.toString().trim()
            val isEmergency = binding.switchUrgency.isChecked

            if (city.isNotEmpty() && hospitalName.isNotEmpty() && contactNumber.isNotEmpty()) {
                viewModel.createRequest(bloodGroup, city, hospitalName, contactNumber, isEmergency)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.requestCreated.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Request created successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Failed to create request", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}