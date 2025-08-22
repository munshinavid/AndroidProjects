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
                putInt("product_image", product.imageResId)
                putString("product_category", product.category) // ✅ add category
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
            arguments?.let { bundle ->
                // ✅ Recreate the Product object with category restored
                val product = Product(
                    name = bundle.getString("product_name") ?: "",
                    price = bundle.getDouble("product_price"),
                    description = bundle.getString("product_description") ?: "",
                    imageResId = bundle.getInt("product_image"),
                    category = bundle.getString("product_category") ?: "" // ✅ fixed
                )

                // Add to cart
                CartManager.addToCart(product)
                Toast.makeText(requireContext(), "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                // Update cart badge
                (requireActivity() as MainActivity).updateCartBadge(CartManager.getCartCount())
            }
        }

        return view
    }
}
