package com.example.todo_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private var todos: MutableList<Todo> // changed to MutableList so we can remove
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]

        holder.itemView.apply {
            val tvTitle = findViewById<TextView>(R.id.tvTitle)
            val cbDone = findViewById<CheckBox>(R.id.cbDone)

            tvTitle.text = currentTodo.title
            cbDone.isChecked = currentTodo.isChecked

            // ✅ When checkbox is clicked
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // remove this item from list
                    todos.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, todos.size)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}
