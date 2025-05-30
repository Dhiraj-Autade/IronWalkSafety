package com.example.menus7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.TouristGuide5.R
import com.google.android.material.snackbar.Snackbar

// Define TransportInfo data class
data class TransportInfo(
    val cardId: Int,
    val title: String,
    val subtitle: String,
    val imageRes: Int
)

class DetailsActivity : AppCompatActivity() {

    private lateinit var btnBackToList: Button
    private lateinit var btnBookNow: Button  // ✅ Added button for booking

    // List of Transport Modes
    private val transportData = listOf(
        TransportInfo(R.id.card_plane, "Plane", "Fastest way to travel.", R.drawable.plane1),
        TransportInfo(R.id.card_train, "Train", "Scenic and comfortable journey.", R.drawable.train),
        TransportInfo(R.id.card_bus, "Bus", "Affordable for long distances.", R.drawable.bus),
        TransportInfo(R.id.card_bike, "Bike", "Perfect for adventure.", R.drawable.bike),
        TransportInfo(R.id.card_ship, "Ship", "Ideal for ocean travel.", R.drawable.ship),
        TransportInfo(R.id.card_car, "Car", "Comfortable private transport.", R.drawable.car)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        btnBackToList = findViewById(R.id.btn_back_to_list)
        btnBookNow = findViewById(R.id.btn_book_now) // ✅ Find the book now button

        // Set up transport cards
        transportData.forEach { transport ->
            val card = findViewById<CardView>(transport.cardId)
            val titleView = card.findViewById<TextView>(R.id.card_title)
            val subtitleView = card.findViewById<TextView>(R.id.card_subtitle)
            val imageView = card.findViewById<ImageView>(R.id.card_icon)

            titleView.text = transport.title
            subtitleView.text = transport.subtitle
            imageView.setImageResource(transport.imageRes)

            // Set default colors (white background, black text)
            card.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            titleView.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            subtitleView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))

            // Click listener to highlight selection
            card.setOnClickListener {
                highlightSelectedCard(transport.cardId, transport.title)
            }
        }


        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        // ✅ Book Now Button Functionality
        btnBookNow.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent) // ✅ Navigates to Booking Page
        }

        // Back button functionality
        btnBackToList.setOnClickListener {
            finish()  // Close activity and return to the previous screen
        }
    }

    // Function to highlight selected card and show Snackbar
    private fun highlightSelectedCard(selectedCardId: Int, transportMode: String) {
        transportData.forEach { transport ->
            val card = findViewById<CardView>(transport.cardId)
            val titleView = card.findViewById<TextView>(R.id.card_title)
            val subtitleView = card.findViewById<TextView>(R.id.card_subtitle)

            if (transport.cardId == selectedCardId) {
                // Set selected card to black background with white text
                card.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
                titleView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                subtitleView.setTextColor(ContextCompat.getColor(this, android.R.color.white))

                // Show Snackbar for selection
                Snackbar.make(card, "$transportMode Selected", Snackbar.LENGTH_SHORT).show()
            } else {
                // Reset other cards to default white background
                card.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
                titleView.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                subtitleView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            }
        }
    }
}
