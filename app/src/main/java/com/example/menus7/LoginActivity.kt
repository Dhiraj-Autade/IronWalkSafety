package com.example.menus7

import android.content.Intent
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        try {
            emailEditText = findViewById(R.id.etUsername)
            passwordEditText = findViewById(R.id.etPassword)
            loginButton = findViewById(R.id.btnLogin)
            signupButton = findViewById(R.id.btnSignup)
            progressBar = findViewById(R.id.progressBar)
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error initializing views: ${e.message}", e)
            Toast.makeText(this, "Error initializing app. Please restart.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Initialize Room Database
        try {
            database = AppDatabase.getDatabase(this)
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error initializing database: ${e.message}", e)
            Toast.makeText(this, "Error initializing app. Please restart.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show progress bar and disable login button
            progressBar.visibility = View.VISIBLE
            loginButton.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // First get user by email
                    val user = database.userDao().getUserByEmail(email)
                    withContext(Dispatchers.Main) {
                        if (user != null && user.password == password) {
                            // User exists and password matches, log them in
                            lifecycleScope.launch(Dispatchers.IO) {
                                try {
                                    database.userDao().updateUserLoggedIn(user.id, true)
                                    withContext(Dispatchers.Main) {
                                        // Start splash screen activity
                                        val intent = Intent(this@LoginActivity, SplashScreenActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        progressBar.visibility = View.GONE
                                        loginButton.isEnabled = true
                                        Log.e("LoginActivity", "Error updating login status: ${e.message}", e)
                                        Toast.makeText(this@LoginActivity, "Error during login. Please try again.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            progressBar.visibility = View.GONE
                            loginButton.isEnabled = true
                            Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        loginButton.isEnabled = true
                        Log.e("LoginActivity", "Error during login: ${e.message}", e)
                        Toast.makeText(this@LoginActivity, "Error during login. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Clear password field for security
        passwordEditText.setText("")
    }
}
