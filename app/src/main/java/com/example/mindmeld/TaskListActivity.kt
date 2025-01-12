package com.example.mindmeld

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskListActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "TaskPrefs"
    private val TASK_KEY = "tasks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val tasks = loadTasksFromSharedPreferences()

        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(tasks, ::deleteTask, ::editTask)
        recyclerView.adapter = taskAdapter

        // Handle the floating action button to add tasks
        val fabAddTask = findViewById<FloatingActionButton>(R.id.fabAddTask)
        fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        // Navigation logic: Back button (ImageView)
        val backImageView = findViewById<ImageView>(R.id.imageView8)  // Make sure imageView8 exists in your layout
        backImageView.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val tasks = loadTasksFromSharedPreferences()
        taskAdapter.updateTasks(tasks)
    }

    private fun loadTasksFromSharedPreferences(): MutableList<Task> {
        val taskListJson = sharedPreferences.getString(TASK_KEY, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Task>>() {}.type
        return Gson().fromJson(taskListJson, type)
    }

    private fun saveTasksToSharedPreferences(tasks: MutableList<Task>) {
        val editor = sharedPreferences.edit()
        val taskListJson = Gson().toJson(tasks)
        editor.putString(TASK_KEY, taskListJson)
        editor.apply()
    }

    private fun deleteTask(position: Int) {
        val tasks = loadTasksFromSharedPreferences()  // Load the latest tasks
        if (position >= 0 && position < tasks.size) {
            tasks.removeAt(position)  // Remove the task from the list
            saveTasksToSharedPreferences(tasks)  // Save the updated task list

            // Update the adapter's data
            taskAdapter.updateTasks(tasks)  // Ensure adapter has the latest list
            taskAdapter.notifyItemRemoved(position)  // Notify adapter about item removal

            Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("TaskListActivity", "Invalid task position: $position")
        }
    }

    // Add the editTask function to handle editing tasks
    private fun editTask(position: Int) {
        val tasks = loadTasksFromSharedPreferences()
        if (position >= 0 && position < tasks.size) {
            val task = tasks[position]
            // Assuming there's an AddTaskActivity to handle editing, we pass the task's data to it.
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("task", Gson().toJson(task)) // Pass the task to the edit activity
            intent.putExtra("task_position", position)  // Pass the position to update the correct task
            startActivity(intent)
        } else {
            Log.e("TaskListActivity", "Invalid task position: $position")
        }
    }
}
