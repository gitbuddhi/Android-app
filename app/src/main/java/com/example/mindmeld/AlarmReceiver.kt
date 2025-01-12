package com.example.mindmeld

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alarm.DestinationActivity

class AlarmReceiver : BroadcastReceiver() {
    private var ringtone: Ringtone? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            // Create the notification channel for Android 8.0 and above
            createNotificationChannel(context)

            // Set up the destination intent for notification tap
            val destinationIntent = Intent(context, DestinationActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, destinationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Get the default alarm sound URI
            val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

            // Play the alarm sound for 5 seconds
            ringtone = RingtoneManager.getRingtone(context, alarmSound)
            ringtone?.play()

            // Stop the alarm sound after 5 seconds
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                ringtone?.stop()
            }, 7000)  // 5 seconds delay to stop the ringtone

            // Build the notification
            val builder = NotificationCompat.Builder(context, "foxandroid")  // Channel ID must match the created channel
                .setSmallIcon(R.drawable.ic_launcher_foreground)  // Replace with your actual icon
                .setContentTitle("Alarm Notification")
                .setContentText("It's time for your alarm!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setVibrate(longArrayOf(0, 1000, 500, 1000)) // Vibrates for 1 second, pauses for 0.5 seconds, and vibrates again

            // Show the notification
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(123, builder.build())
        }
    }

    // Helper function to create the notification channel
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "AlarmChannel"
            val descriptionText = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("foxandroid", name, importance).apply {
                description = descriptionText
                enableVibration(true) // Enable vibration for this channel
                enableLights(true) // Optionally enable LED lights
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
