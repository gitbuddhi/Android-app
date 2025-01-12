package com.example.mindmeld

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "TaskPrefs"
    private val TASK_KEY = "tasks"
    private var taskPosition: Int = -1  // For editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val editTextTime = findViewById<EditText>(R.id.editTextTime)
        val saveButton = findViewById<Button>(R.id.buttonSave)

        taskPosition = intent.getIntExtra("task_position", -1)
        if (taskPosition != -1) {
            loadTaskForEditing(taskPosition, editTextDescription, editTextDate, editTextTime)
        }

        saveButton.setOnClickListener {
            val description = editTextDescription.text.toString()
            val date = editTextDate.text.toString()
            val time = editTextTime.text.toString()

            if (description.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                saveTask(Task(description, date, time))
                finish()
            } else {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadTaskForEditing(position: Int, description: EditText, date: EditText, time: EditText) {
        val tasks = loadTasksFromSharedPreferences()
        val task = tasks[position]
        description.setText(task.description)
        date.setText(task.date)
        time.setText(task.time)
    }

    private fun saveTask(task: Task) {
        val tasks = loadTasksFromSharedPreferences()

        if (taskPosition != -1) {
            tasks[taskPosition] = task
        } else {
            tasks.add(task)
        }

        saveTasksToSharedPreferences(tasks)

        Toast.makeText(this, "Task added. Will remind you after 1 minute.", Toast.LENGTH_SHORT).show()

        scheduleReminder(task)
    }

    private fun loadTasksFromSharedPreferences(): MutableList<Task> {
        val taskListJson = sharedPreferences.getString(TASK_KEY, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Task>>() {}.type
        return Gson().fromJson(taskListJson, type)
    }

    private fun saveTasksToSharedPreferences(tasks: MutableList<Task>) {
        val editor = sharedPreferences.edit()
        val tasksJson = Gson().toJson(tasks)
        editor.putString(TASK_KEY, tasksJson)
        editor.apply()
    }

    private fun scheduleReminder(task: Task) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("task_description", task.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this, task.hashCode(), reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Trger after 1 minute (60,000 milliseconds)
        val triggerTime = System.currentTimeMillis() + 600

        // Schedule the alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

}
