package com.example.bloodlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.R
import com.example.bloodlink.data.BloodRequest

class BloodRequestAdapter(private var requests: List<BloodRequest>) : RecyclerView.Adapter<BloodRequestAdapter.BloodRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blood_request, parent, false)
        return BloodRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: BloodRequestViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = requests.size

    fun updateRequests(newRequests: List<BloodRequest>) {
        requests = newRequests
        notifyDataSetChanged()
    }

    inner class BloodRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bloodGroupTextView: TextView = itemView.findViewById(R.id.blood_group)
        private val cityTextView: TextView = itemView.findViewById(R.id.city)
        private val hospitalTextView: TextView = itemView.findViewById(R.id.hospital)
        private val contactButton: Button = itemView.findViewById(R.id.contact_button)

        fun bind(request: BloodRequest) {
            bloodGroupTextView.text = request.bloodGroup
            cityTextView.text = request.city
            hospitalTextView.text = request.hospital

            contactButton.setOnClickListener {
                // Handle contact button click
                Toast.makeText(itemView.context, "Contacting ${request.contactNumber}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}