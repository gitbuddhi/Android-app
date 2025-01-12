package com.example.mindmeld

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskDescription = intent.getStringExtra("task_description") ?: "Task Reminder"

        // Generate a 3-second beep sound
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100) // Volume set to 100
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 3000) // Beep for 3 seconds

        // Show the notification
        showNotification(context, taskDescription)
    }

    private fun showNotification(context: Context, taskDescription: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_reminder_channel",
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "task_reminder_channel")
            .setSmallIcon(R.drawable.baseline_access_time_filled_24)
            .setContentTitle("Task Reminder")
            .setContentText("Reminder for: $taskDescription")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
