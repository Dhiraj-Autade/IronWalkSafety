package com.example.menus7.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.example.menus7.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    fun showSuccessSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.success_green))
        snackbar.setTextColor(ContextCompat.getColor(view.context, android.R.color.white))
        snackbar.setActionTextColor(ContextCompat.getColor(view.context, android.R.color.white))
        snackbar.setAction("OK") { snackbar.dismiss() }
        
        val snackbarView = snackbar.view
        snackbarView.elevation = 6f
        snackbar.show()
    }

    fun showAlertSentSnackbar(view: View) {
        showSuccessSnackbar(view, "🚨 Alert sent successfully! Help is on the way!")
    }

    fun showLocationSharedSnackbar(view: View) {
        showSuccessSnackbar(view, "📍 Location shared successfully! Stay safe!")
    }
} 