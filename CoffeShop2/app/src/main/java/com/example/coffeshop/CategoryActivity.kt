package com.example.coffeshop

import android.os.Bundle
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CategoryActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView

    private val categories = listOf(
        "Coffee",
        "Tea",
        "Smoothies",
        "Pastries",
        "Sandwiches"
    )

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        listView = findViewById(R.id.categoriesListView)
        searchView = findViewById(R.id.categorySearchView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        listView.adapter = adapter

        // Item click
        listView.setOnItemClickListener { _, _, position, _ ->
            val clickedCategory = adapter.getItem(position)
            Toast.makeText(this, "Clicked: $clickedCategory", Toast.LENGTH_SHORT).show()
            // Here you can open Product screen for this category
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val clickedCategory = adapter.getItem(position)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("category", clickedCategory) // Pass selected category
            startActivity(intent)
        }


        // Search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }
}
