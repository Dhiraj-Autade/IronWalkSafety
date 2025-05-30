package com.example.menus7

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    private val TAG = "SplashScreenActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Hide the action bar
        supportActionBar?.hide()

        // Delay for 3 seconds then start MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                Log.d(TAG, "Starting MainActivity...")
                // Start MainActivity with a clean task to prevent back navigation
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Log.d(TAG, "MainActivity started successfully")
                finish()
            } catch (e: Exception) {
                Log.e(TAG, "Error starting MainActivity: ${e.message}", e)
                // If there's an error, try to go back to login
                try {
                    Log.d(TAG, "Attempting to start LoginActivity as fallback...")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Log.d(TAG, "LoginActivity started successfully")
                } catch (e2: Exception) {
                    Log.e(TAG, "Failed to start LoginActivity: ${e2.message}", e2)
                }
                finish()
            }
        }, 3000) // 3000 milliseconds = 3 seconds
    }

    override fun onBackPressed() {
        // Disable back button during splash screen
    }
}
