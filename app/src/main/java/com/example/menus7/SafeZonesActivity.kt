package com.example.menus7

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import android.graphics.Color
import android.util.Log

class SafeZonesActivity : AppCompatActivity() {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private val TAG = "SafeZonesActivity"
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Sample safe zones data (in a real app, this would come from a database)
    private val safeZones = listOf(
        SafeZone("Police Station", LatLng(12.9716, 77.5946), "24/7 police station with emergency services"),
        SafeZone("Hospital", LatLng(12.9717, 77.5947), "Emergency medical services available"),
        SafeZone("Women's Shelter", LatLng(12.9718, 77.5948), "Safe shelter for women in emergency situations"),
        SafeZone("Shopping Mall", LatLng(12.9719, 77.5949), "Public place with security personnel"),
        SafeZone("Bus Station", LatLng(12.9720, 77.5950), "Public transportation hub with security")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safe_zones)

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Setup toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Safe Zones"

        // Setup search UI
        searchButton = findViewById(R.id.search_button)
        searchEditText = findViewById(R.id.search_edit_text)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchSafeZones(query)
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }

        // Setup map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            setupMap()
            
            // Check location permission
            if (checkLocationPermission()) {
                enableMyLocation()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun setupMap() {
        // Draw all safe zones
        safeZones.forEach { safeZone ->
            // Add marker for the safe zone
            map.addMarker(
                MarkerOptions()
                    .position(safeZone.location)
                    .title(safeZone.name)
                    .snippet(safeZone.description)
            )
            
            // Draw a circle around the safe zone (representing its area)
            val radius = 100.0 // meters
            val points = createCirclePoints(safeZone.location, radius)
            
            map.addPolygon(
                PolygonOptions()
                    .addAll(points)
                    .fillColor(Color.argb(128, 0, 255, 0))
                    .strokeColor(Color.GREEN)
                    .strokeWidth(2f)
            )
        }

        // Move camera to show all safe zones
        val bounds = LatLngBounds.builder()
        safeZones.forEach { bounds.include(it.location) }
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50))
    }

    private fun createCirclePoints(center: LatLng, radius: Double): List<LatLng> {
        val points = mutableListOf<LatLng>()
        val numPoints = 36 // Number of points to create a circle
        
        for (i in 0 until numPoints) {
            val angle = i * (360.0 / numPoints)
            val lat = center.latitude + (radius / 111320.0) * Math.cos(Math.toRadians(angle))
            val lng = center.longitude + (radius / (111320.0 * Math.cos(Math.toRadians(center.latitude)))) * Math.sin(Math.toRadians(angle))
            points.add(LatLng(lat, lng))
        }
        
        return points
    }

    private fun searchSafeZones(query: String) {
        // Clear existing markers
        map.clear()
        
        // Filter safe zones based on query
        val filteredZones = safeZones.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true) 
        }
        
        if (filteredZones.isEmpty()) {
            Toast.makeText(this, "No safe zones found matching '$query'", Toast.LENGTH_SHORT).show()
            // Show all safe zones again
            setupMap()
            return
        }
        
        // Draw filtered safe zones
        filteredZones.forEach { safeZone ->
            // Add marker for the safe zone
            map.addMarker(
                MarkerOptions()
                    .position(safeZone.location)
                    .title(safeZone.name)
                    .snippet(safeZone.description)
            )
            
            // Draw a circle around the safe zone
            val radius = 100.0 // meters
            val points = createCirclePoints(safeZone.location, radius)
            
            map.addPolygon(
                PolygonOptions()
                    .addAll(points)
                    .fillColor(Color.argb(128, 0, 255, 0))
                    .strokeColor(Color.GREEN)
                    .strokeWidth(2f)
            )
        }

        // Move camera to show filtered safe zones
        val bounds = LatLngBounds.builder()
        filteredZones.forEach { bounds.include(it.location) }
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50))
    }

    private fun checkLocationPermission(): Boolean {
        return LOCATION_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            LOCATION_PERMISSIONS,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun enableMyLocation() {
        if (checkLocationPermission()) {
            try {
                map.isMyLocationEnabled = true
                getLastLocation()
            } catch (e: Exception) {
                Log.e(TAG, "Error enabling my location: ${e.message}", e)
            }
        }
    }

    private fun getLastLocation() {
        if (!checkLocationPermission()) return

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    updateMapLocation(location)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting last location: ${e.message}", e)
        }
    }

    private fun updateMapLocation(location: Location) {
        try {
            val latLng = LatLng(location.latitude, location.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Your Location")
                    .snippet("You are here")
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } catch (e: Exception) {
            Log.e(TAG, "Error updating map location: ${e.message}", e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    enableMyLocation()
                } else {
                    Toast.makeText(this, "Location permission is required for safety features", Toast.LENGTH_LONG).show()
                }
            }
        }
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
    
    // Data class for safe zones
    data class SafeZone(
        val name: String,
        val location: LatLng,
        val description: String
    )
} 