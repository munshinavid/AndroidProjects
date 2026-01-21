package com.example.findblood.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.findblood.R
import com.example.findblood.data.model.Request

class RequestAdapter(
    private var requestList: List<Request>,
    private val isDonor: Boolean,
    private val onApprove: (Request) -> Unit,
    private val onReject: (Request) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requestList[position]
        holder.bloodGroup.text = request.bloodGroup
        holder.hospital.text = request.hospitalName
        holder.status.text = request.status

        if (isDonor) {
            holder.approveButton.visibility = View.GONE
            holder.rejectButton.visibility = View.GONE
            if (request.urgency == "emergency") {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFCDD2")) // A light red color
            }
        } else {
            if (request.status == "pending") {
                holder.approveButton.visibility = View.VISIBLE
                holder.rejectButton.visibility = View.VISIBLE
            } else {
                holder.approveButton.visibility = View.GONE
                holder.rejectButton.visibility = View.GONE
            }
        }

        holder.approveButton.setOnClickListener { onApprove(request) }
        holder.rejectButton.setOnClickListener { onReject(request) }
    }

    override fun getItemCount() = requestList.size

    fun updateRequests(requests: List<Request>) {
        this.requestList = requests
        notifyDataSetChanged()
    }

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bloodGroup: TextView = itemView.findViewById(R.id.tvBloodGroup)
        val hospital: TextView = itemView.findViewById(R.id.tvHospital)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
        val approveButton: Button = itemView.findViewById(R.id.btnApprove)
        val rejectButton: Button = itemView.findViewById(R.id.btnReject)
    }
}