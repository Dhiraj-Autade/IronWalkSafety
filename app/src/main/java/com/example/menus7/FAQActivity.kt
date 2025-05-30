package com.example.menus7

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat

class FAQActivity : AppCompatActivity() {
    private val faqs = listOf(
        FAQ("What is IronWalk?", "IronWalk is a women's safety app that provides real-time location tracking, emergency contacts, safe zones, and quick access to emergency services."),
        FAQ("How do I add emergency contacts?", "Go to the Emergency Contacts section and tap the + button. Enter the contact's name and phone number. You can add multiple contacts for different situations."),
        FAQ("What are Safe Zones?", "Safe Zones are pre-defined areas marked as safe on the map. These could be police stations, hospitals, or other trusted locations. The app will notify you when you enter or leave these zones."),
        FAQ("How does the SOS button work?", "The SOS button sends your current location to all your emergency contacts via SMS. It also triggers phone vibration and can be customized to send alerts to nearby authorities."),
        FAQ("Can I share my location with trusted contacts?", "Yes, use the Share Location button in the menu to share your current location with anyone via messaging apps or email."),
        FAQ("How do I book transport?", "Use the Transport Options menu to access Ola or Uber directly from the app. This ensures safe and tracked transportation."),
        FAQ("Is my location data secure?", "Yes, we use end-to-end encryption for all location data. Your information is only shared with your explicitly chosen emergency contacts."),
        FAQ("What should I do in an emergency?", "1. Press the SOS button\n2. Try to move to the nearest Safe Zone\n3. Use the Share Location feature\n4. Contact emergency services (100)")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "FAQ"

        val recyclerView = findViewById<RecyclerView>(R.id.faq_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FAQAdapter(faqs)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    data class FAQ(val question: String, val answer: String)

    private inner class FAQAdapter(private val faqs: List<FAQ>) : 
        RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

        inner class FAQViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val question: TextView = view.findViewById(R.id.question_text)
            val answer: TextView = view.findViewById(R.id.answer_text)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_faq, parent, false)
            return FAQViewHolder(view)
        }

        override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
            val faq = faqs[position]
            holder.question.text = faq.question
            holder.answer.text = faq.answer
        }

        override fun getItemCount() = faqs.size
    }
} 