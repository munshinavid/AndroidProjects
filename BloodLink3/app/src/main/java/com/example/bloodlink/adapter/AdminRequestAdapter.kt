package com.example.bloodlink.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodlink.R
import com.example.bloodlink.data.BloodRequest

class AdminRequestAdapter(
    private var requests: List<BloodRequest>,
    private val onApproveClicked: (BloodRequest) -> Unit,
    private val onRejectClicked: (BloodRequest) -> Unit
) : RecyclerView.Adapter<AdminRequestAdapter.AdminRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_request, parent, false)
        return AdminRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminRequestViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = requests.size

    fun updateRequests(newRequests: List<BloodRequest>) {
        requests = newRequests
        notifyDataSetChanged()
    }

    inner class AdminRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bloodGroupTextView: TextView = itemView.findViewById(R.id.blood_group)
        private val locationTextView: TextView = itemView.findViewById(R.id.location)
        private val requesterNameTextView: TextView = itemView.findViewById(R.id.requester_name)
        private val urgencyTextView: TextView = itemView.findViewById(R.id.urgency)
        private val approveButton: Button = itemView.findViewById(R.id.approve_button)
        private val rejectButton: Button = itemView.findViewById(R.id.reject_button)

        fun bind(request: BloodRequest) {
            bloodGroupTextView.text = request.bloodGroup
            locationTextView.text = "${request.city}, ${request.hospital}"
            requesterNameTextView.text = request.requesterName

            if (request.isEmergency) {
                urgencyTextView.text = "EMERGENCY"
                urgencyTextView.setTextColor(Color.RED)
            } else {
                urgencyTextView.text = ""
            }

            approveButton.setOnClickListener { onApproveClicked(request) }
            rejectButton.setOnClickListener { onRejectClicked(request) }
        }
    }
}