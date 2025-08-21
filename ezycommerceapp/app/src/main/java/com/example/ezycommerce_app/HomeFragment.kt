package com.example.ezycommerce_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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

        // Dummy products (later you can load from API/database)
        productList = listOf(
            Product("Shoes", 199.0, "Comfortable running shoes", R.drawable.shoes),
            Product("Watch", 99.0, "Stylish wrist watch", R.drawable.watch),
            Product("T-Shirt", 49.0, "Durable travel bag", R.drawable.tshirt)
        )

        // ✅ HERE you put your adapter with click handling
        productAdapter = ProductAdapter(productList) { clickedProduct ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProductDetailsFragment.newInstance(clickedProduct))
                .addToBackStack(null) // so back button works
                .commit()
        }

        recyclerView.adapter = productAdapter

        return view
    }
}
