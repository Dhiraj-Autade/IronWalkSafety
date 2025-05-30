package com.example.menus7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.TouristGuide5.R

class DestinationAdapter(
    private var destinations: List<Destination>, // Use var so we can modify the list
    private val onItemClick: (Destination) -> Unit
) : RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_destination, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = destinations[position]
        holder.bind(destination, onItemClick)
    }

    override fun getItemCount() = destinations.size

    class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_destination)
        private val nameTextView: TextView = itemView.findViewById(R.id.text_destination_name)
        private val locationTextView: TextView = itemView.findViewById(R.id.text_destination_location)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.text_destination_description)

        fun bind(destination: Destination, onItemClick: (Destination) -> Unit) {
            imageView.setImageResource(destination.imageRes)
            nameTextView.text = destination.name
            locationTextView.text = destination.location
            descriptionTextView.text = destination.description // ✅ Ensure description is shown

            itemView.setOnClickListener {
                onItemClick(destination)
            }
        }
    }

    // ✅ Function to update the adapter list dynamically
    fun updateList(newList: List<Destination>) {
        destinations = newList
        notifyDataSetChanged() // Refresh RecyclerView with new data
    }
}
