package com.example.bloodlink.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.R
import com.example.bloodlink.data.BloodRequest

class MyRequestAdapter(private var requests: List<BloodRequest>) : RecyclerView.Adapter<MyRequestAdapter.MyRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_request, parent, false)
        return MyRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRequestViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = requests.size

    fun updateRequests(newRequests: List<BloodRequest>) {
        requests = newRequests
        notifyDataSetChanged()
    }

    inner class MyRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bloodGroupTextView: TextView = itemView.findViewById(R.id.blood_group)
        private val hospitalNameTextView: TextView = itemView.findViewById(R.id.hospital_name)
        private val statusTextView: TextView = itemView.findViewById(R.id.status)
        private val statusIndicator: View = itemView.findViewById(R.id.status_indicator)

        fun bind(request: BloodRequest) {
            bloodGroupTextView.text = request.bloodGroup
            hospitalNameTextView.text = request.hospital
            statusTextView.text = request.status

            val statusColor = when (request.status) {
                "pending" -> R.color.yellow
                "approved" -> R.color.green
                "rejected" -> R.color.red
                else -> android.R.color.darker_gray
            }
            statusIndicator.setBackgroundColor(ContextCompat.getColor(itemView.context, statusColor))
        }
    }
}