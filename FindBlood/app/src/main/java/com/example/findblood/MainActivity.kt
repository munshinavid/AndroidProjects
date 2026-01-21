package com.example.findblood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.findblood.databinding.ActivityMainBinding
import com.example.findblood.ui.AdminDashboardFragment
import com.example.findblood.ui.DonorDashboardFragment
import com.example.findblood.ui.RequesterDashboardFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = intent.getStringExtra("USER_ROLE")

        when (userRole) {
            "donor" -> replaceFragment(DonorDashboardFragment())
            "requester" -> replaceFragment(RequesterDashboardFragment())
            "admin" -> replaceFragment(AdminDashboardFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}