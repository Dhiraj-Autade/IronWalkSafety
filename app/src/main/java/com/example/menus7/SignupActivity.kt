package com.example.menus7

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.menus7.data.AppDatabase
import com.example.menus7.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var googleSignupButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize views
        try {
            fullNameEditText = findViewById(R.id.etFullName)
            emailEditText = findViewById(R.id.etEmail)
            passwordEditText = findViewById(R.id.etPassword)
            signupButton = findViewById(R.id.btnSignup)
            progressBar = findViewById(R.id.progressBar)
        } catch (e: Exception) {
            Log.e("SignupActivity", "Error initializing views: ${e.message}", e)
            Toast.makeText(this, "Error initializing app. Please restart.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Initialize Room Database
        try {
            database = AppDatabase.getDatabase(this)
        } catch (e: Exception) {
            Log.e("SignupActivity", "Error initializing database: ${e.message}", e)
            Toast.makeText(this, "Error initializing app. Please restart.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        signupButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show progress bar and disable signup button
            progressBar.visibility = View.VISIBLE
            signupButton.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Check if email already exists
                    val existingUser = database.userDao().getUserByEmail(email)
                    withContext(Dispatchers.Main) {
                        if (existingUser != null) {
                            progressBar.visibility = View.GONE
                            signupButton.isEnabled = true
                            Toast.makeText(this@SignupActivity, "Email already exists", Toast.LENGTH_SHORT).show()
                            return@withContext
                        }

                        // Create new user
                        val user = User(
                            username = fullName,
                            email = email,
                            password = password,
                            isLoggedIn = false
                        )
                        
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                database.userDao().insertUser(user)
                                withContext(Dispatchers.Main) {
                                    progressBar.visibility = View.GONE
                                    signupButton.isEnabled = true
                                    Toast.makeText(this@SignupActivity, "Sign up successful! Please login.", Toast.LENGTH_SHORT).show()
                                    // Return to login activity
                                    finish()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    progressBar.visibility = View.GONE
                                    signupButton.isEnabled = true
                                    Log.e("SignupActivity", "Error during user insertion: ${e.message}", e)
                                    Toast.makeText(this@SignupActivity, "Error during signup. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        signupButton.isEnabled = true
                        Log.e("SignupActivity", "Error during signup: ${e.message}", e)
                        Toast.makeText(this@SignupActivity, "Error during signup. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        // Clear password field for security
        passwordEditText.setText("")
    }
}
