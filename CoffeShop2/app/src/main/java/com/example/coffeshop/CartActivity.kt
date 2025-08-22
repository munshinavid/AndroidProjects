package com.example.coffeshop

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val listView = findViewById<ListView>(R.id.cartListView)
        val totalPriceView = findViewById<TextView>(R.id.totalPrice)
        val checkoutButton = findViewById<Button>(R.id.checkoutButton)

        // Convert items to "Name - Price" strings for the ListView
        val items = CartManager.getItems().map { "${it.first} - ${it.second}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        totalPriceView.text = "$%.2f".format(CartManager.getTotal())

        checkoutButton.setOnClickListener {
            Toast.makeText(this, "Checkout not implemented yet!", Toast.LENGTH_SHORT).show()
        }
    }
}
