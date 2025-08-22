package com.example.coffeshop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var productGrid: GridView
    private lateinit var categoriesContainer: LinearLayout
    private lateinit var searchInput: EditText
    private lateinit var cartIcon: ImageView
    private lateinit var profileIcon: ImageView
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        productGrid = findViewById(R.id.productGrid)
        categoriesContainer = findViewById(R.id.categoriesContainer)
        searchInput = findViewById(R.id.searchInput)
        cartIcon = findViewById(R.id.cartIcon)
        profileIcon = findViewById(R.id.profileIcon)

        val products = mutableListOf(
            Product(R.drawable.laptop, "Laptop Pro 14", "$999", "Electronics", "High-performance laptop"),
            Product(R.drawable.headphones, "Wireless Headphones", "$129", "Electronics", "Noise-cancelling"),
            Product(R.drawable.watch, "Smart Watch S", "$179", "Electronics", "Fitness tracker"),
            Product(R.drawable.tshirt, "Cotton T-Shirt", "$19", "Fashion", "100% cotton"),
            Product(R.drawable.shoes, "Running Shoes", "$59", "Fashion", "Lightweight and comfy"),
            Product(R.drawable.tshirt, "Casual Shirt", "$25", "Fashion", "Perfect for casual occasions")
        )

        adapter = ProductAdapter(this, products)
        productGrid.adapter = adapter

        // Show single "Categories" button
        setupCategoryButton()

        // Grid item click
        productGrid.setOnItemClickListener { _, _, position, _ ->
            val clicked = adapter.getItem(position) as Product
            Toast.makeText(this, "Clicked: ${clicked.name}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("product", clicked)
            startActivity(intent)
        }

        // Cart click -> open CartActivity
        cartIcon.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Profile click -> open ProfileActivity
        profileIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Search filter
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filterByText(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupCategoryButton() {
        categoriesContainer.removeAllViews()

        val btn = Button(this).apply {
            text = "Categories"
            setPadding(32, 16, 32, 16)
            setOnClickListener {
                // Open CategoryActivity
                val intent = Intent(this@MainActivity, CategoryActivity::class.java)
                startActivity(intent)
            }
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(16, 16, 16, 16) }

        categoriesContainer.addView(btn, params)
    }
}
