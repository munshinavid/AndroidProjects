package com.example.coffeshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ProductAdapter(
    private val context: Context,
    private val products: MutableList<Product>
) : BaseAdapter() {

    private var filteredProducts: MutableList<Product> = products.toMutableList()

    override fun getCount(): Int = filteredProducts.size
    override fun getItem(position: Int): Any = filteredProducts[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.product_item, parent, false
        )

        val product = filteredProducts[position]

        val img = view.findViewById<ImageView>(R.id.productImage)
        val name = view.findViewById<TextView>(R.id.productName)
        val price = view.findViewById<TextView>(R.id.productPrice)

        img.setImageResource(product.imageRes)
        name.text = product.name
        price.text = product.price

        return view
    }

    fun filterByText(query: String) {
        filteredProducts = if (query.isEmpty()) {
            products.toMutableList()
        } else {
            products.filter {
                it.name.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun filterByCategory(category: String) {
        filteredProducts = if (category == "All") {
            products.toMutableList()
        } else {
            products.filter { it.category.equals(category, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }
}
