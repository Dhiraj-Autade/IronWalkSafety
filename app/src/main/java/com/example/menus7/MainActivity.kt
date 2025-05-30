package com.example.menus7

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menus7.data.AppDatabase
import com.example.menus7.utils.SnackbarUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var sosButton: FloatingActionButton
    private lateinit var webView: WebView
    private var currentLocation: Location? = null
    private val TAG = "MainActivity"
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                currentLocation = location
                updateMapLocation(location)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
            // Set status bar to transparent
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            // Make status bar icons dark for better visibility
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            // Initialize components
            initializeViews()
            initializeDatabase()
            initializeLocation()
            
            // Check login status
            checkLoginStatus()
            
            // Setup components
            setupWebView()
            setupMap()
            setupSosButton()
            
            // Request permissions
            requestPermissions()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing app. Please restart.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun initializeViews() {
        try {
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.title = "IronWalk"

            webView = findViewById(R.id.web_view)
            sosButton = findViewById(R.id.fab)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views: ${e.message}", e)
            throw e
        }
    }
    
    private fun initializeDatabase() {
        try {
            database = AppDatabase.getDatabase(this)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing database: ${e.message}", e)
            throw e
        }
        }

    private fun initializeLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                database.userDao().getLoggedInUser().collect { user ->
                    withContext(Dispatchers.Main) {
                    if (user == null) {
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error checking login status: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error checking login status", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupWebView() {
        try {
            // Configure WebView settings
            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_NO_CACHE
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportMultipleWindows(true)
                javaScriptCanOpenWindowsAutomatically = true
            }

            // Set WebView client to handle page loading
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                    
                    // Inject CSS to ensure content is scrollable
                    val css = """
                        javascript:(function() {
                            var style = document.createElement('style');
                            style.type = 'text/css';
                            style.innerHTML = 'body { overflow-y: scroll !important; -webkit-overflow-scrolling: touch !important; }';
                            document.head.appendChild(style);
                        })()
                    """.trimIndent()
                    
                    view?.loadUrl(css)
                }
            }

            // Set WebView chrome client to handle JavaScript dialogs
            webView.webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    result?.confirm()
                    return true
                }
            }

            // Load a women's safety related website
            webView.loadUrl("https://www.unwomen.org/en/what-we-do/ending-violence-against-women")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up WebView: ${e.message}", e)
        }
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            try {
                map.uiSettings.isZoomControlsEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
                
                if (checkLocationPermission()) {
                    enableMyLocation()
                } else {
                    requestLocationPermission()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error setting up map: ${e.message}", e)
            }
        }
    }

    private fun setupSosButton() {
        sosButton.setImageResource(android.R.drawable.ic_dialog_alert)
        sosButton.setOnClickListener {
            showSosDialog()
        }
    }

    private fun showSosDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_sos_alert, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set click listeners for each emergency type
        dialogView.findViewById<View>(R.id.harassment_card).setOnClickListener {
            dialog.dismiss()
            showContactSelectionDialog("Harassment")
        }

        dialogView.findViewById<View>(R.id.stalking_card).setOnClickListener {
            dialog.dismiss()
            showContactSelectionDialog("Stalking")
        }

        dialogView.findViewById<View>(R.id.unsafe_situation_card).setOnClickListener {
            dialog.dismiss()
            showContactSelectionDialog("Unsafe Situation")
        }

        dialogView.findViewById<View>(R.id.medical_emergency_card).setOnClickListener {
            dialog.dismiss()
            showContactSelectionDialog("Medical Emergency")
        }

        dialogView.findViewById<View>(R.id.other_card).setOnClickListener {
            dialog.dismiss()
            showContactSelectionDialog("Other")
        }

        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // Show dialog with a slide-up animation
        dialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
        dialog.show()
    }
    
    private fun showContactSelectionDialog(incidentType: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_contact_selection, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set click listeners for each option
        dialogView.findViewById<View>(R.id.emergency_contacts_card).setOnClickListener {
            dialog.dismiss()
            triggerSosAlert(incidentType, "emergency_contacts")
        }

        dialogView.findViewById<View>(R.id.specific_number_card).setOnClickListener {
            dialog.dismiss()
            triggerSosAlert(incidentType, "specific_number")
        }

        dialogView.findViewById<View>(R.id.all_contacts_card).setOnClickListener {
            dialog.dismiss()
            triggerSosAlert(incidentType, "all_contacts")
        }

        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // Show dialog with a slide-up animation
        dialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
        dialog.show()
    }

    private fun triggerSosAlert(incidentType: String, contactType: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val location = currentLocation
                
                if (location != null) {
                    // Get user email for the alert message
                    val user = database.userDao().getLoggedInUser().first()
                    val userEmail = user?.email ?: "User"
                    
                    val message = """
                        🚨 EMERGENCY ALERT! 🚨
                        $userEmail is in danger and needs help!
                        Incident Type: $incidentType
                        Last known location: https://www.google.com/maps?q=${location.latitude},${location.longitude}
                        
                        This is an automated alert from IronWalk App.
                        Please respond immediately if you can help.
                    """.trimIndent()

                    withContext(Dispatchers.Main) {
                        // Vibrate phone
                        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            @Suppress("DEPRECATION")
                            vibrator.vibrate(1000)
                        }

                        // Send SMS based on contact type
                        val smsManager = SmsManager.getDefault()
                        
                        when (contactType) {
                            "emergency_contacts" -> {
                                val contacts = database.emergencyContactDao().getPrimaryContacts().first()
                                contacts.forEach { contact ->
                                    try {
                                        smsManager.sendTextMessage(contact.phone, null, message, null, null)
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error sending SMS to ${contact.name}: ${e.message}")
                                    }
                                }
                                SnackbarUtils.showAlertSentSnackbar(findViewById(android.R.id.content))
                            }
                            "specific_number" -> {
                                try {
                                    smsManager.sendTextMessage("9518719036", null, message, null, null)
                                    SnackbarUtils.showAlertSentSnackbar(findViewById(android.R.id.content))
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error sending SMS to specific number: ${e.message}")
                                    Toast.makeText(this@MainActivity, "Error sending alert to specific number", Toast.LENGTH_SHORT).show()
                                }
                            }
                            "all_contacts" -> {
                                // Share via intent to allow user to choose contacts
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, message)
                                }
                                startActivity(Intent.createChooser(shareIntent, "Share SOS Alert"))
                                SnackbarUtils.showAlertSentSnackbar(findViewById(android.R.id.content))
                            }
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        if (checkLocationPermission()) {
                            getLastLocation()
                        } else {
                            requestLocationPermission()
                        }
                        Toast.makeText(this@MainActivity, "Unable to get location. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in triggerSosAlert: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error sending alert", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (!checkLocationPermission()) return

        try {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateDistanceMeters(10f)
                .build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                mainLooper
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error starting location updates: ${e.message}", e)
        }
    }

    private fun stopLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping location updates: ${e.message}", e)
        }
    }

    private fun updateMapLocation(location: Location) {
        try {
            val latLng = LatLng(location.latitude, location.longitude)
            
            // Clear previous markers
            map.clear()
            
            // Add a marker for the current location
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Your Location")
                    .snippet("You are here")
            )
            
            // Move camera to the current location
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } catch (e: Exception) {
            Log.e(TAG, "Error updating map location: ${e.message}", e)
        }
    }

    private fun getLastLocation() {
        if (!checkLocationPermission()) return

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                    updateMapLocation(location)
                } else {
                    startLocationUpdates()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting last location: ${e.message}", e)
        }
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

    private fun requestPermissions() {
        if (!checkLocationPermission()) {
            requestLocationPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkLocationPermission()) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_share -> {
                shareCurrentLocation()
                true
            }
            R.id.action_transport_options -> {
                showTransportOptions()
                true
            }
            R.id.action_faq -> {
                startActivity(Intent(this, FAQActivity::class.java))
                true
            }
            R.id.action_contact_support -> {
                contactSupport()
                true
            }
            R.id.action_privacy_policy -> {
                openPrivacyPolicy()
                true
            }
            R.id.action_logout -> {
                logout()
                true
            }
            R.id.action_emergency_contacts -> {
                showEmergencyContacts()
                true
            }
            R.id.action_safe_zones -> {
                startActivity(Intent(this, SafeZonesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareCurrentLocation() {
        currentLocation?.let { location ->
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = database.userDao().getLoggedInUser().first()
                    val userEmail = user?.email ?: "User"
                    
                    val locationUri = "https://www.google.com/maps?q=${location.latitude},${location.longitude}"
                    val message = """
                        🚨 EMERGENCY ALERT! 🚨
                        $userEmail needs help!
                        Last known location: $locationUri
                        
                        This is an automated alert from IronWalk App.
                        Please respond immediately if you can help.
                    """.trimIndent()
                    
                    withContext(Dispatchers.Main) {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, message)
                        }
                        startActivity(Intent.createChooser(shareIntent, "Share Location"))
                        SnackbarUtils.showLocationSharedSnackbar(findViewById(android.R.id.content))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error sharing location: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Error sharing location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } ?: run {
            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTransportOptions() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_transport_options, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set click listeners for each transport option
        dialogView.findViewById<View>(R.id.ola_card).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.olacabs.com"))
            startActivity(intent)
        }

        dialogView.findViewById<View>(R.id.uber_card).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://m.uber.com"))
            startActivity(intent)
        }

        dialogView.findViewById<View>(R.id.irctc_card).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.irctc.co.in"))
                    startActivity(intent)
        }

        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // Show dialog with a slide-up animation
        dialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
        dialog.show()
    }

    private fun contactSupport() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:1800-XXX-XXXX")
        startActivity(intent)
    }

    private fun openPrivacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.example.com/privacy-policy")
        startActivity(intent)
    }

    private fun logout() {
        // Clear user session
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply()
        
        // Start login activity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
                    finish()
                }

    private fun showEmergencyContacts() {
        val contacts = listOf(
            EmergencyContact("Police", "100", android.R.drawable.ic_menu_call),
            EmergencyContact("Women's Helpline", "1091", android.R.drawable.ic_menu_call),
            EmergencyContact("Emergency", "112", android.R.drawable.ic_menu_call),
            EmergencyContact("Ambulance", "108", android.R.drawable.ic_menu_call),
            EmergencyContact("Fire", "101", android.R.drawable.ic_menu_call)
        )
        
        val dialogView = layoutInflater.inflate(R.layout.dialog_emergency_contacts, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.emergency_contacts_recycler_view)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_cancel)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = EmergencyContactAdapter(contacts) { contact ->
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${contact.number}")
            startActivity(intent)
        }
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    data class EmergencyContact(
        val name: String,
        val number: String,
        val iconResId: Int
    )
}
