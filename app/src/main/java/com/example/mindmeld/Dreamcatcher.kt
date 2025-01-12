package com.example.mindmeld

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Dreamcatcher : AppCompatActivity() {

    // Define the SharedPreferences file name
    private val PREFS_NAME = "DreamPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dreamcatcher)

        // Setup for edge-to-edge window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dremblog)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the EditText fields
        val dreamAbout = findViewById<EditText>(R.id.dreamabout)
        val dreamDescription = findViewById<EditText>(R.id.dreamdescription)
        val dreamDate = findViewById<EditText>(R.id.dreamdate)
        val dreamTime = findViewById<EditText>(R.id.dreamtime)

        // Back button to navigate to homepage
        val arrowback = findViewById<ImageView>(R.id.arrowbk)
        arrowback.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }

        // Search button stores data in SharedPreferences and moves to dreamresult page
        val searchButton = findViewById<Button>(R.id.dreamsearch)
        searchButton.setOnClickListener {
            // Retrieve the text from the EditText fields
            val about = dreamAbout.text.toString()
            val description = dreamDescription.text.toString()
            val date = dreamDate.text.toString()
            val time = dreamTime.text.toString()

            // Validate inputs before saving
            if (about.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save data into SharedPreferences
                saveToSharedPreferences(about, description, date, time)

                // Show confirmation message
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()

                // Navigate to dreamresult activity
                val intent = Intent(this, dreamresult::class.java)
                startActivity(intent)
            }
        }
    }

    // Function to store data in SharedPreferences
    private fun saveToSharedPreferences(about: String, description: String, date: String, time: String) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("dream_about", about)
        editor.putString("dream_description", description)
        editor.putString("dream_date", date)
        editor.putString("dream_time", time)

        // Apply the changes asynchronously
        editor.apply()
    }
}
