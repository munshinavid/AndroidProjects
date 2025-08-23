package com.example.todo_list


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Pre-made credentials (for showcase)
        val correctUsername = "admin"
        val correctPassword = "1234"

        loginButton.setOnClickListener {
            val enteredUsername = usernameInput.text.toString()
            val enteredPassword = passwordInput.text.toString()

            if (enteredUsername == correctUsername && enteredPassword == correctPassword) {
                // Success → Go to MainActivity
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // prevent going back to login
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
