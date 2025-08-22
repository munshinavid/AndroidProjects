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
        val allProducts = listOf(
            Product("Shoes", 199.0, "Comfortable running shoes", R.drawable.shoes, "Clothing"),
            Product("Watch", 99.0, "Stylish wrist watch", R.drawable.watch, "Accessories"),
            Product("T-Shirt", 49.0, "Casual cotton t-shirt", R.drawable.tshirt, "Clothing"),
            Product("Laptop", 1200.0, "Gaming laptop", R.drawable.laptop, "Electronics"),
            Product("Apple", 2.0, "Fresh apple", R.drawable.watch, "Grocery")
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
