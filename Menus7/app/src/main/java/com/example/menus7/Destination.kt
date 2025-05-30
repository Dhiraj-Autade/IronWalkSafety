package com.example.menus7

data class Destination(
    val name: String,         // Name of the destination
    val location: String,     // Location (City/State)
    val imageRes: Int,        // Drawable resource ID for the image
    val description: String   // Description of the place
)
