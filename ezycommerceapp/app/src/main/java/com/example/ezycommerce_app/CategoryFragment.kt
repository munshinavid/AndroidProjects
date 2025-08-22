package com.example.ezycommerce_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class CategoryFragment : Fragment() {

    private val categories = listOf("Electronics", "Clothing", "Grocery", "Books", "Shoes")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val listView: ListView = view.findViewById(R.id.categoryListView)

        // simple adapter to show text list
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        listView.adapter = adapter

        // handle click
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categories[position]

            // show only related products
            val fragment = HomeFragment.newInstance(selectedCategory)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
