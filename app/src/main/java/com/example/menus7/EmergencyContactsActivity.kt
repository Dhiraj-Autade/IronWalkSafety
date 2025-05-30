package com.example.menus7

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menus7.data.AppDatabase
import com.example.menus7.data.EmergencyContact
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmergencyContactsActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var addContactButton: FloatingActionButton
    private val TAG = "EmergencyContactsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contacts)
        window.statusBarColor = resources.getColor(R.color.black, theme)

        try {
            // Set up toolbar
            val toolbar: Toolbar = findViewById(R.id.toolbar_emergency_contacts)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Emergency Contacts"

            // Initialize database
            database = AppDatabase.getDatabase(this)

            // Set up RecyclerView
            recyclerView = findViewById(R.id.recycler_view_contacts)
            recyclerView.layoutManager = LinearLayoutManager(this)

            // Set up add contact button
            addContactButton = findViewById(R.id.fab_add_contact)
            addContactButton.setOnClickListener {
                showAddContactDialog()
            }

            // Load contacts
            loadContacts()

        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing contacts. Please try again.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                database.emergencyContactDao().getAllContacts().collect { contacts ->
                    withContext(Dispatchers.Main) {
                        if (contacts.isEmpty()) {
                            findViewById<View>(R.id.tv_no_contacts).visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        } else {
                            findViewById<View>(R.id.tv_no_contacts).visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            // TODO: Set up RecyclerView adapter
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading contacts: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EmergencyContactsActivity, "Error loading contacts", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showAddContactDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.et_contact_name)
        val phoneInput = dialogView.findViewById<EditText>(R.id.et_contact_phone)
        val emailInput = dialogView.findViewById<EditText>(R.id.et_contact_email)

        AlertDialog.Builder(this)
            .setTitle("Add Emergency Contact")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString().trim()
                val phone = phoneInput.text.toString().trim()
                val email = emailInput.text.toString().trim()

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    addContact(name, phone, email.ifEmpty { null })
                } else {
                    Toast.makeText(this, "Name and phone are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addContact(name: String, phone: String, email: String?) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val contact = EmergencyContact(
                    name = name,
                    phone = phone,
                    email = email,
                    isPrimary = true
                )
                database.emergencyContactDao().insertContact(contact)
                
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EmergencyContactsActivity, "Contact added successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error adding contact: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EmergencyContactsActivity, "Error adding contact", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 