package com.example.campusfix

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RequestsAdapter(
    private val onItemClick: (RepairRequest) -> Unit
) : RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

    private var requestsList: List<RepairRequest> = emptyList()

    fun submitList(list: List<RepairRequest>) {
        requestsList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repair_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requestsList[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = requestsList.size

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textviewIssue: TextView = itemView.findViewById(R.id.textviewIssue)
        private val textviewLocation: TextView = itemView.findViewById(R.id.textviewLocation)
        private val textviewStatus: TextView = itemView.findViewById(R.id.textviewStatus)
        private val textviewDescription: TextView = itemView.findViewById(R.id.textviewDescription)

        fun bind(request: RepairRequest) {
            textviewIssue.text = request.issue
            textviewLocation.text = request.location
            textviewDescription.text = request.description
            textviewStatus.text = request.status

            when (request.status) {
                "Pending" -> textviewStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F8C400")) // Gold
                "In Progress" -> textviewStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#89343B")) // Maroon
                "Completed" -> textviewStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50")) // Green
            }

            itemView.setOnClickListener {
                onItemClick(request)
            }
        }
    }
}
