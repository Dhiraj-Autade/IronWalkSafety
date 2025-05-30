package com.example.menus7

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmergencyContactAdapter(
    private val contacts: List<MainActivity.EmergencyContact>,
    private val onContactClick: (MainActivity.EmergencyContact) -> Unit
) : RecyclerView.Adapter<EmergencyContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.contact_icon)
        val name: TextView = itemView.findViewById(R.id.contact_name)
        val number: TextView = itemView.findViewById(R.id.contact_number)
        val callButton: ImageButton = itemView.findViewById(R.id.btn_call)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onContactClick(contacts[position])
                }
            }

            callButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = contacts[position]
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${contact.number}")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emergency_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.icon.setImageResource(contact.iconResId)
        holder.name.text = contact.name
        holder.number.text = contact.number
    }

    override fun getItemCount() = contacts.size
} 