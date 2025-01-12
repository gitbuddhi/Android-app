package com.example.mindmeld

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class homepage : AppCompatActivity() {

    private val POST_NOTIFICATIONS_REQUEST_CODE = 1001
    private lateinit var notificationBell: ImageView
    private var alarmTriggered = false  // State to track if the alarm has been triggered

    private val alarmReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.mindmeld.ALARM_TRIGGERED") {
                showNotificationAlert()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)

        notificationBell = findViewById(R.id.notificationid)

        // Request notification permission if needed
        requestNotificationPermission()

        // Register broadcast receiver for the alarm
        registerReceiver(alarmReceiver, IntentFilter("com.example.mindmeld.ALARM_TRIGGERED"))

        // Adjust the window insets for the layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dremblog)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigate to other pages (same as before)
        setupNavigation()
    }

    private fun setupNavigation() {
        val dreamcatchButton = findViewById<Button>(R.id.dreamcatch)
        dreamcatchButton.setOnClickListener {
            val intent = Intent(this, Dreamcatcher::class.java)
            startActivity(intent)
        }

        val summaryButton = findViewById<Button>(R.id.summary)
        summaryButton.setOnClickListener {
            val intent = Intent(this, dreamresult::class.java)
            startActivity(intent)
        }

        val blogButton = findViewById<Button>(R.id.blogbut)
        blogButton.setOnClickListener {
            val intent = Intent(this, logDream::class.java)
            startActivity(intent)
        }

        val taskListButton = findViewById<Button>(R.id.taskListButton)
        taskListButton.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

        // Fix the alarm button navigation
        val alarmManager = findViewById<ImageView>(R.id.showAlarmButton)
        alarmManager.setOnClickListener {
            val intent = Intent(this, showAlarm::class.java)  // Make sure it's `showAlarm`
            startActivity(intent)
        }

        val timerButton = findViewById<Button>(R.id.timerButton)
        timerButton.setOnClickListener {
            val intent = Intent(this, TimerStopwatchActivity::class.java)
            startActivity(intent)
        }
    }

    // Request notification permission if needed
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), POST_NOTIFICATIONS_REQUEST_CODE)
            }
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == POST_NOTIFICATIONS_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, notifications can be shown
            } else {
                Toast.makeText(this, "Notification permission required!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Update the notification bell to show an alert (badge or change icon)
    private fun showNotificationAlert() {
        // Option 1: Change the icon (e.g., add a red circle as an alert)
        notificationBell.setImageResource(R.drawable.baseline_settings_24)  // Assuming you have a different drawable for the alert state
        alarmTriggered = true  // Update the state

        Toast.makeText(this, "Alarm triggered! Check notifications.", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // You can check if the alarm was triggered previously and update UI if necessary
        if (alarmTriggered) {
            showNotificationAlert()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(alarmReceiver)  // Unregister the receiver when the activity is destroyed
    }
}
