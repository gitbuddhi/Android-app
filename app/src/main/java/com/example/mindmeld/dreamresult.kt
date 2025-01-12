package com.example.mindmeld

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class dreamresult : AppCompatActivity() {

    private val PREFS_NAME = "DreamPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dreamresult)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dremblog)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up Edit Button to navigate to EditDreamActivity
        val editButton = findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val intent = Intent(this, EditDreamActivity::class.java)
            startActivity(intent)
        }

        // Delete Button functionality with confirmation dialog
        val deleteButton = findViewById<Button>(R.id.deletebutton)
        deleteButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val aboutTextView = findViewById<TextView>(R.id.textView18)
            val descriptionTextView = findViewById<TextView>(R.id.textView19)
            val dateTextView = findViewById<TextView>(R.id.textView20)
            val timeTextView = findViewById<TextView>(R.id.textView21)
            showDeleteConfirmationDialog(sharedPreferences, aboutTextView, descriptionTextView, dateTextView, timeTextView)
        }

        // Set up navigation buttons
        val logoImageView = findViewById<ImageView>(R.id.dishomebtn)
        logoImageView.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }

        val backImageView = findViewById<ImageView>(R.id.resbackarr)
        backImageView.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data every time the activity comes into view
        loadDreamData()
    }

    // Function to load and display dream data
    private fun loadDreamData() {
        // Retrieve data from SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dreamAbout = sharedPreferences.getString("dream_about", "No data")
        val dreamDescription = sharedPreferences.getString("dream_description", "No data")
        val dreamDate = sharedPreferences.getString("dream_date", "No data")
        val dreamTime = sharedPreferences.getString("dream_time", "No data")

        // Find the TextViews and display the data
        val aboutTextView = findViewById<TextView>(R.id.textView18)
        val descriptionTextView = findViewById<TextView>(R.id.textView19)
        val dateTextView = findViewById<TextView>(R.id.textView20)
        val timeTextView = findViewById<TextView>(R.id.textView21)

        aboutTextView.text = dreamAbout
        descriptionTextView.text = dreamDescription
        dateTextView.text = dreamDate
        timeTextView.text = dreamTime
    }

    // Function to show a confirmation dialog before deleting dream data
    private fun showDeleteConfirmationDialog(
        sharedPreferences: SharedPreferences,
        aboutTextView: TextView,
        descriptionTextView: TextView,
        dateTextView: TextView,
        timeTextView: TextView
    ) {
        // Create an AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Dream")
        builder.setMessage("Are you sure you want to delete this dream data?")

        // Set up Yes button
        builder.setPositiveButton("Yes") { dialog, _ ->
            // If user clicks Yes, delete the data
            deleteDreamData(sharedPreferences)
            clearUI(aboutTextView, descriptionTextView, dateTextView, timeTextView)
            Toast.makeText(this, "Dream data deleted successfully!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()  // Close the dialog
        }

        // Set up No button
        builder.setNegativeButton("No") { dialog, _ ->
            // If user clicks No, just dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

    // Function to delete the dream data from SharedPreferences
    private fun deleteDreamData(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.clear()  // Remove all saved dream data
        editor.apply()   // Apply the changes asynchronously
    }

    // Function to clear the UI after deleting data
    private fun clearUI(aboutTextView: TextView, descriptionTextView: TextView, dateTextView: TextView, timeTextView: TextView) {
        aboutTextView.text = ""
        descriptionTextView.text = ""
        dateTextView.text = ""
        timeTextView.text = ""
    }
}
