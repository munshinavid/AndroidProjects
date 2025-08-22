package com.example.coffeshop

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        Toast.makeText(this, "Details Activity Opened", Toast.LENGTH_SHORT).show()
        //return

        val product = intent.getParcelableExtra<Product>("product")

        val img = findViewById<ImageView>(R.id.productImage)
        val name = findViewById<TextView>(R.id.productName)
        val price = findViewById<TextView>(R.id.productPrice)
        val desc = findViewById<TextView>(R.id.productDescription)
        val addButton = findViewById<Button>(R.id.addToCartButton)

        // Check for null before using the views
        if (img == null || name == null || price == null || desc == null || addButton == null) {
            Toast.makeText(this, "Layout Error: One or more views are missing!", Toast.LENGTH_LONG).show()
            // Log a detailed error so you can see which one is null
            println("ProductDetailsActivity: A view is null. Check your XML IDs!")
            return // Stop execution to prevent further errors
        }

        product?.let {
            img.setImageResource(it.imageRes)
            name.text = it.name
            price.text = it.price
            desc.text = "This is a sample description for ${it.description}."
        }

        addButton.setOnClickListener {
            if (product != null) {
                CartManager.addItem(product.name, product.price)
                Toast.makeText(this, "${product.name} added to cart!", Toast.LENGTH_SHORT).show()

                // Optional: Go directly to cart after adding
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                }
        }
    }
}
