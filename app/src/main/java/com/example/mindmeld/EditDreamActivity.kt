package com.example.mindmeld

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditDreamActivity : AppCompatActivity() {

    private val PREFS_NAME = "DreamPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_dream)

        // Retrieve the existing data from SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dreamAbout = sharedPreferences.getString("dream_about", "")
        val dreamDescription = sharedPreferences.getString("dream_description", "")
        val dreamDate = sharedPreferences.getString("dream_date", "")
        val dreamTime = sharedPreferences.getString("dream_time", "")

        // Find EditTexts and pre-fill with current data
        val aboutEditText = findViewById<EditText>(R.id.dreamAboutEditText)
        val descriptionEditText = findViewById<EditText>(R.id.dreamDescriptionEditText)
        val dateEditText = findViewById<EditText>(R.id.dreamDateEditText)
        val timeEditText = findViewById<EditText>(R.id.dreamTimeEditText)

        aboutEditText.setText(dreamAbout)
        descriptionEditText.setText(dreamDescription)
        dateEditText.setText(dreamDate)
        timeEditText.setText(dreamTime)

        // Save button to update SharedPreferences
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Get the updated data
            val editedAbout = aboutEditText.text.toString()
            val editedDescription = descriptionEditText.text.toString()
            val editedDate = dateEditText.text.toString()
            val editedTime = timeEditText.text.toString()

            // Save the updated data in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("dream_about", editedAbout)
            editor.putString("dream_description", editedDescription)
            editor.putString("dream_date", editedDate)
            editor.putString("dream_time", editedTime)
            editor.apply()

            // Notify the user
            Toast.makeText(this, "Dream details updated successfully!", Toast.LENGTH_SHORT).show()

            // Go back to the previous activity
            finish()
        }
    }
}
