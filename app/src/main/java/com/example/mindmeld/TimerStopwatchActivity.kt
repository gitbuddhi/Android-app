package com.example.mindmeld

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class TimerStopwatchActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var minuteInput: EditText
    private lateinit var secondInput: EditText
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    private var isTimerRunning = false
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0  // Start at 0 for proper reset to 00:00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_stopwatch)

        // Initialize UI elements
        timerTextView = findViewById(R.id.timerTextView)
        minuteInput = findViewById(R.id.minuteInput)
        secondInput = findViewById(R.id.secondInput)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)

        // Start Button (Timer)
        startButton.setOnClickListener {
            if (!isTimerRunning) {
                startTimer()
            }
        }

        // Stop Button
        stopButton.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            }
        }

        // Reset Button
        resetButton.setOnClickListener {
            resetTimer()
        }

        // Set up ImageView navigation to homepage
        val imageView = findViewById<ImageView>(R.id.imageView2)
        imageView.setOnClickListener {
            val intent = Intent(this, homepage::class.java)  // Replace with your homepage activity class
            startActivity(intent)
        }
    }

    // Start Timer Function
    private fun startTimer() {
        // Only set new time if it's the first time or after reset
        if (timeLeftInMillis == 0L) {
            val minutes = minuteInput.text.toString().toLongOrNull() ?: 0
            val seconds = secondInput.text.toString().toLongOrNull() ?: 0
            timeLeftInMillis = (minutes * 60 + seconds) * 1000
        }

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                isTimerRunning = false
                startButton.isEnabled = true
                stopButton.isEnabled = false
                resetButton.isEnabled = true

                // Show toast
                Toast.makeText(this@TimerStopwatchActivity, "Time is up!", Toast.LENGTH_SHORT).show()

                // Play a beep sound for 3 seconds
                playBeepSound()

                // Show a notification
                showNotification("Time is up!")
            }
        }.start()

        isTimerRunning = true
        startButton.isEnabled = false
        stopButton.isEnabled = true
        resetButton.isEnabled = false
    }

    // Play Beep Sound
    private fun playBeepSound() {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100) // Volume level at 100
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 3000) // Play beep for 3 seconds (3000 ms)
    }

    // Show Notification
    private fun showNotification(message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // For Android O and higher, a notification channel is required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "timer_channel"
            val channel = NotificationChannel(channelId, "Timer Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "timer_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)  // Replace with your app icon
            .setContentTitle("Timer Alert")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    // Pause Timer
    private fun pauseTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        startButton.isEnabled = true
        stopButton.isEnabled = false
        resetButton.isEnabled = true
    }

    // Reset Timer
    private fun resetTimer() {
        timeLeftInMillis = 0L  // Set time to zero
        updateTimerText()  // Update the UI to show 00:00:00
        minuteInput.setText("")
        secondInput.setText("")
        startButton.isEnabled = true
        stopButton.isEnabled = false
        resetButton.isEnabled = false
    }

    // Update Timer TextView
    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        timerTextView.text = String.format("%02d:%02d", minutes, seconds)
    }
}
