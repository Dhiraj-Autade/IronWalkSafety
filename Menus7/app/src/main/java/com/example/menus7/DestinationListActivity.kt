package com.example.menus7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView // Ensure correct import
import com.example.TouristGuide5.R

class DestinationListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DestinationAdapter
    private lateinit var searchView: SearchView

    // Original list of destinations
    private val destinations = listOf(
        Destination("Raigad Fort", "Maharashtra", R.drawable.raigad, "Historic fort known for its connection to Chhatrapati Shivaji Maharaj."),
        Destination("Konkan", "Ratnagiri, Maharashtra", R.drawable.konkan, "A beautiful coastal region with serene beaches and breathtaking landscapes."),
        Destination("Sahyadri", "Maharashtra", R.drawable.sahyadri, "A scenic mountain range, perfect for trekking and nature lovers."),
        Destination("Mahabaleshwar", "Satara, Maharashtra", R.drawable.mahabaleshwar, "A famous hill station known for its strawberries and stunning viewpoints."),
        Destination("Lonavla", "Pune, Maharashtra", R.drawable.lonavla, "Popular for its lush green landscapes and monsoon waterfalls."),
        Destination("Mumbai", "Maharashtra", R.drawable.mumbai2, "The financial capital of India, home to Bollywood and Marine Drive."),
        Destination("Ellora Caves", "Ch.Shambhajinagar, Maharashtra", R.drawable.ellora, "A UNESCO World Heritage Site, famous for its ancient rock-cut caves."),
        Destination("Vengurla", "Konkan, Maharashtra", R.drawable.vengurla1, "A coastal town with pristine beaches and coconut groves."),
        Destination("Lalbagh cha Raja", "Mumbai, Maharashtra", R.drawable.lalbagh, "One of the most famous Ganesh idols in Mumbai during Ganesh Chaturthi.")
    )



    // Mutable list for filtering search results
    private var filteredDestinations = destinations.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_list)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_destinations)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize SearchView
        searchView = findViewById(R.id.search_view_destinations)

        // Set up adapter with filtered list
        adapter = DestinationAdapter(filteredDestinations) { selectedDestination ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("destinationName", selectedDestination.name)
            intent.putExtra("destinationDescription", selectedDestination.description)
            startActivity(intent)
        }

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        recyclerView.adapter = adapter

        // Set up SearchView functionality
        setupSearchView()

        // Back button functionality
        val backButton = findViewById<Button>(R.id.btn_back_to_main)
        backButton.setOnClickListener {
            finish() // Close activity and return to the previous screen
        }
    }



    // Function to initialize search functionality
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Filtering happens in real-time, no need for submission action
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDestinations(newText)
                return true
            }
        })
    }



    // Function to filter destinations based on search input
    private fun filterDestinations(query: String?) {
        filteredDestinations.clear() // Clear the previous search results
        if (!query.isNullOrEmpty()) {
            val lowerCaseQuery = query.lowercase()
            filteredDestinations.addAll(destinations.filter {
                it.name.lowercase().contains(lowerCaseQuery) || it.location.lowercase().contains(lowerCaseQuery)
            })
        } else {
            filteredDestinations.addAll(destinations) // Show all items when search is cleared
        }
        adapter.updateList(filteredDestinations) // ✅ Correctly update the adapter's list
    }
}
