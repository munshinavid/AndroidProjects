package com.example.ezycommerce_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProductDetailsFragment : Fragment() {

    companion object {
        fun newInstance(product: Product): ProductDetailsFragment {
            val fragment = ProductDetailsFragment()
            val bundle = Bundle().apply {
                putString("product_name", product.name)
                putDouble("product_price", product.price)
                putString("product_description", product.description)
                putInt("product_image", product.imageResId) // ✅ image from drawable
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_details, container, false)

        val imageView = view.findViewById<ImageView>(R.id.productImage)
        val nameView = view.findViewById<TextView>(R.id.productName)
        val priceView = view.findViewById<TextView>(R.id.productPrice)
        val descView = view.findViewById<TextView>(R.id.productDescription)
        val addButton = view.findViewById<Button>(R.id.addToCartButton)

        arguments?.let {
            nameView.text = it.getString("product_name")
            priceView.text = "$${it.getDouble("product_price")}"
            descView.text = it.getString("product_description")
            imageView.setImageResource(it.getInt("product_image"))
        }

        addButton.setOnClickListener {
            Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
