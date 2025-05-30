package com.example.menus7

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.TouristGuide5.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*

class BookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)  // Ensure this matches your XML file name

        val spinnerDestinations = findViewById<Spinner>(R.id.spinner_destinations)
        val datePickerButton = findViewById<Button>(R.id.btn_pick_date)
        val dateTextView = findViewById<TextView>(R.id.tv_selected_date)
        val timePickerButton = findViewById<Button>(R.id.btn_pick_time)
        val timeTextView = findViewById<TextView>(R.id.tv_selected_time)
        val radioGroupGender = findViewById<RadioGroup>(R.id.radio_group_gender)
        val emailEditText = findViewById<EditText>(R.id.et_email)
        val chipGroupPreferences = findViewById<ChipGroup>(R.id.chip_group_preferences)
        val ratingBar = findViewById<RatingBar>(R.id.rating_platform)
        val submitButton = findViewById<Button>(R.id.btn_submit_booking)
        val imageViewDestination = findViewById<ImageView>(R.id.img_destination)

        // Set default image (Ensure this exists in drawable folder)
        imageViewDestination.setImageResource(R.drawable.whitem)

        // Destination Spinner Data
        val destinations = arrayOf("Select Destination", "Raigad Fort", "Konkan", "Sahyadri", "Mahabaleshwar", "Lonavla", "Mumbai", "Ellora Caves", "Vengurla", "Lalbagh cha Raja")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, destinations)
        spinnerDestinations.adapter = adapter

        // Date Picker
        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                dateTextView.text = "Selected Date: $selectedDay/${selectedMonth + 1}/$selectedYear"
            }, year, month, day)

            datePickerDialog.show()
        }


        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        // Time Picker
        timePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                timeTextView.text = "Selected Time: ${String.format("%02d:%02d", selectedHour, selectedMinute)}"
            }, hour, minute, true)

            timePickerDialog.show()
        }

        // Submit Booking
        submitButton.setOnClickListener {
            val selectedDestination = spinnerDestinations.selectedItem.toString()
            val selectedDate = dateTextView.text.toString().replace("Selected Date: ", "").trim()
            val selectedTime = timeTextView.text.toString().replace("Selected Time: ", "").trim()
            val email = emailEditText.text.toString().trim()
            val ratingBar = findViewById<RatingBar>(R.id.rating_platform)
            ratingBar.progressDrawable.setTint(resources.getColor(android.R.color.white))

            val selectedGender = when (radioGroupGender.checkedRadioButtonId) {
                R.id.radio_male -> "Male"
                R.id.radio_female -> "Female"
                R.id.radio_other -> "Other"
                else -> "Not Selected"
            }

            // Get Selected Chips
            val selectedChips = mutableListOf<String>()
            for (i in 0 until chipGroupPreferences.childCount) {
                val chip = chipGroupPreferences.getChildAt(i) as Chip
                if (chip.isChecked) {
                    selectedChips.add(chip.text.toString())
                }
            }

            if (selectedDestination == "Select Destination" || selectedDate.isEmpty() || selectedTime.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Confirm Booking")
                    .setMessage(
                        "Booking Confirmed!\nDestination: $selectedDestination\nDate: $selectedDate\nTime: $selectedTime\nGender: $selectedGender\nPreferences: ${selectedChips.joinToString()}\n"
                    )
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }
}
