package com.example.todo_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private var todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]

        // Bind text and checkbox state
        holder.tvTitle.text = currentTodo.title
        holder.cbDone.isChecked = currentTodo.isChecked

        // ⚠️ First, clear any old listener to avoid multiple triggers
        holder.cbDone.setOnCheckedChangeListener(null)

        // ✅ Then attach new listener
        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update state
                currentTodo.isChecked = true
                // Remove safely
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    todos.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
        }
    }

    override fun getItemCount(): Int = todos.size
}
