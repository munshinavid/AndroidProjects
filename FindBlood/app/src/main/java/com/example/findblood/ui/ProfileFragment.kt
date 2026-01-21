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
import com.example.findblood.databinding.FragmentProfileBinding
import com.example.findblood.viewmodel.DonorViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DonorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DonorViewModel::class.java)

        val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, bloodGroups)
        binding.spinnerBloodGroup.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.etName.setText(it.name)
                binding.etPhone.setText(it.phone)
                binding.etCity.setText(it.city)
                val bloodGroupPosition = bloodGroups.indexOf(it.bloodGroup)
                if (bloodGroupPosition >= 0) {
                    binding.spinnerBloodGroup.setSelection(bloodGroupPosition)
                }
            }
        }

        binding.btnUpdateProfile.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val bloodGroup = binding.spinnerBloodGroup.selectedItem.toString()
            val city = binding.etCity.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty() && city.isNotEmpty()) {
                viewModel.updateProfile(name, phone, bloodGroup, city)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.profileUpdated.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchCurrentUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}