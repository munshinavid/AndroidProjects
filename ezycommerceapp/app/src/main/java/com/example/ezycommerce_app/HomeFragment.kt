package com.example.ezycommerce_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: List<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // ✅ Dummy products (later you can load from API/database)
        // ✅ Dummy products (later you can load from API/database)
        val allProducts = listOf(
            // Original Products
            Product("Shoes", 199.0, "Comfortable running shoes", R.drawable.shoes, "Clothing"),
            Product("Watch", 99.0, "Stylish wrist watch", R.drawable.watch, "Accessories"),
            Product("T-Shirt", 49.0, "Casual cotton t-shirt", R.drawable.tshirt, "Clothing"),
            Product("Laptop", 1200.0, "Gaming laptop", R.drawable.laptop, "Electronics"),
            Product("Apple", 2.0, "Fresh apple", R.drawable.watch, "Grocery"),
            Product("Keyboard", 75.0, "Mechanical keyboard", R.drawable.laptop, "Electronics"),
            Product("Headphones", 150.0, "Noise-cancelling headphones", R.drawable.laptop, "Electronics"),
            Product("Jeans", 80.0, "Blue denim jeans", R.drawable.shoes, "Clothing"),
            Product("Banana", 1.5, "Organic banana", R.drawable.watch, "Grocery"),

            // Added 11 more products to reach a total of 20
            Product("Smartphone", 899.0, "Latest model smartphone", R.drawable.laptop, "Electronics"),
            Product("Tablet", 550.0, "10-inch HD display tablet", R.drawable.laptop, "Electronics"),
            Product("Socks", 15.0, "A pair of athletic socks", R.drawable.shoes, "Clothing"),
            Product("Jacket", 150.0, "Waterproof winter jacket", R.drawable.tshirt, "Clothing"),
            Product("Sunglasses", 65.0, "Polarized designer sunglasses", R.drawable.watch, "Accessories"),
            Product("Bracelet", 40.0, "Silver charm bracelet", R.drawable.watch, "Accessories"),
            Product("Bread", 3.5, "Freshly baked whole wheat bread", R.drawable.watch, "Grocery"),
            Product("Milk", 4.0, "One gallon of fresh milk", R.drawable.watch, "Grocery"),
            Product("Mouse", 30.0, "Wireless ergonomic mouse", R.drawable.laptop, "Electronics"),
            Product("Desktop PC", 1500.0, "High-performance desktop computer", R.drawable.laptop, "Electronics"),
            Product("Shorts", 35.0, "Athletic training shorts", R.drawable.tshirt, "Clothing")
        )

        // ✅ Check if category was passed
        val selectedCategory = arguments?.getString("category")

        productList = if (!selectedCategory.isNullOrEmpty()) {
            allProducts.filter { it.category == selectedCategory }
        } else {
            allProducts
        }

        // ✅ Set adapter with click handling
        productAdapter = ProductAdapter(productList) { clickedProduct ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProductDetailsFragment.newInstance(clickedProduct))
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = productAdapter

        return view
    }

    companion object {
        fun newInstance(selectedCategory: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle().apply {
                putString("category", selectedCategory)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
