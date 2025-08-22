package com.example.ezycommerce_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalTextView: TextView
    private lateinit var placeOrderButton: Button
    private lateinit var cartAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        recyclerView = view.findViewById(R.id.cartRecyclerView)
        totalTextView = view.findViewById(R.id.totalTextView)
        placeOrderButton = view.findViewById(R.id.placeOrderButton)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cartAdapter = ProductAdapter(CartManager.cartItems) { clickedProduct ->
            // Optional: handle item click in cart
        }
        recyclerView.adapter = cartAdapter

        updateTotal()

        placeOrderButton.setOnClickListener {
            if (CartManager.cartItems.isNotEmpty()) {
                CartManager.clearCart()
                cartAdapter.notifyDataSetChanged()
                updateTotal()
                Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Cart is empty!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun updateTotal() {
        val total = CartManager.cartItems.sumOf { it.price }
        totalTextView.text = "Total: $${String.format("%.2f", total)}"
    }
}
