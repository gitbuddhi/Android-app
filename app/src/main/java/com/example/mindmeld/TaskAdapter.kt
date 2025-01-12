package com.example.mindmeld

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onDeleteClick: (Int) -> Unit,
    private val onEditClick: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder to represent each task item
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        val timeTextView: TextView = itemView.findViewById(R.id.textViewTime)
        val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflate the layout for individual task item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.descriptionTextView.text = task.description
        holder.dateTextView.text = task.date
        holder.timeTextView.text = task.time

        // Set listeners for Edit and Delete buttons
        holder.editButton.setOnClickListener { onEditClick(position) }
        holder.deleteButton.setOnClickListener { onDeleteClick(position) }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    // Method to update the list of tasks and notify the adapter to refresh the RecyclerView
    fun updateTasks(newTasks: MutableList<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()  // This will refresh the whole list in case of major changes
    }

}
