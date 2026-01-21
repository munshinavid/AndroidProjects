package com.example.findblood.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.findblood.R
import com.example.findblood.data.model.Donation
import java.text.SimpleDateFormat
import java.util.Locale

class DonationAdapter(private var donationList: List<Donation>) : RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_donation, parent, false)
        return DonationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val donation = donationList[position]
        holder.hospitalName.text = donation.hospitalName
        holder.donationDate.text = donation.donationDate?.toDate()?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        }
    }

    override fun getItemCount() = donationList.size

    fun updateDonations(donations: List<Donation>) {
        this.donationList = donations
        notifyDataSetChanged()
    }

    class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val donationDate: TextView = itemView.findViewById(R.id.tvDonationDate)
    }
}