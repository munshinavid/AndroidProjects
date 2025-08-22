package com.example.coffeshop

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val profileName = findViewById<TextView>(R.id.profileName)
        val ordersListView = findViewById<ListView>(R.id.ordersListView)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Set dummy profile data
        profileImage.setImageResource(R.drawable.ic_profile) // drawable image
        profileName.text = "John Doe"

        // Dummy orders list
        val orders = listOf(
            "Order #1001 - Laptop Pro 14",
            "Order #1002 - Wireless Headphones",
            "Order #1003 - Smart Watch S",
            "Order #1004 - Cotton T-Shirt"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orders)
        ordersListView.adapter = adapter

        // Logout button click
        logoutButton.setOnClickListener {
            Toast.makeText(this, "Logged out (demo)", Toast.LENGTH_SHORT).show()
            // Add your logout logic here
        }
    }
}
