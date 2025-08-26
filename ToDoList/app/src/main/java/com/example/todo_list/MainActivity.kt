package com.example.todo_list

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Dummy todos
        val todoList = mutableListOf(
            Todo("Follow AndroidDevs", false),
            Todo("Learn about RecyclerView", true),
            Todo("Feed my cat", false),
            Todo("Prank my boss", false),
            Todo("Eat some curry", true),
            Todo("Ask my crush out", false),
            Todo("Take a shower", false)
        )

        val adapter = TodoAdapter(todoList)

        val rvTodos = findViewById<RecyclerView>(R.id.rvTodos)
        val etTodo = findViewById<EditText>(R.id.etTodo)
        val btnAddTodo = findViewById<Button>(R.id.btnAddTodo)

        rvTodos.adapter = adapter
        rvTodos.layoutManager = LinearLayoutManager(this)

        // ✅ Add new todo on button click
        btnAddTodo.setOnClickListener {
            val todoText = etTodo.text.toString().trim()
            if (todoText.isNotEmpty()) {
                val newTodo = Todo(todoText, false)
                todoList.add(newTodo)
                adapter.notifyItemInserted(todoList.size - 1)
                rvTodos.scrollToPosition(todoList.size - 1) // scroll to last item
                etTodo.text.clear()
            } else {
                Toast.makeText(this, "Please enter a todo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
