package com.example.mindmeld

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class AlarmManagerActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var timePicker: TimePicker

    private val POST_NOTIFICATIONS_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_manager)

        // Request notification permission if needed
        requestNotificationPermission()

        // Initialize AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Find UI elements
        timePicker = findViewById(R.id.timePicker)
        val setAlarmButton = findViewById<Button>(R.id.setAlarmButton)

        // Set onClickListener for the alarm button
        setAlarmButton.setOnClickListener {
            setAlarm()
        }

        // Set up navigation for the home button (ImageView)
        val homeButton = findViewById<ImageView>(R.id.homebtn)
        homeButton.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }
    }

    private fun setAlarm() {
        Log.d("AlarmManagerActivity", "setAlarm() called")
        Toast.makeText(this, "Setting Alarm...", Toast.LENGTH_SHORT).show()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePicker.hour)
            set(Calendar.MINUTE, timePicker.minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        Log.d("AlarmManagerActivity", "Alarm set for: ${calendar.time}")

        // Create an intent for the AlarmReceiver
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Set the alarm with allowWhileIdle for Doze Mode
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(this, "Alarm set for ${timePicker.hour}:${timePicker.minute}", Toast.LENGTH_SHORT).show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), POST_NOTIFICATIONS_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == POST_NOTIFICATIONS_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted
            } else {
                // Handle permission denial
            }
        }
    }
}
