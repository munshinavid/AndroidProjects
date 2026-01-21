package com.example.bloodlink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nameEditText = findViewById<EditText>(R.id.name)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val phoneEditText = findViewById<EditText>(R.id.phone)
        val bloodGroupEditText = findViewById<EditText>(R.id.bloodGroup)
        val cityEditText = findViewById<EditText>(R.id.city)
        val roleGroup = findViewById<RadioGroup>(R.id.role_group)
        val registerButton = findViewById<Button>(R.id.register_button)
        val loginPrompt = findViewById<TextView>(R.id.login_prompt)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val bloodGroup = bloodGroupEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val selectedRoleId = roleGroup.checkedRadioButtonId

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty() && bloodGroup.isNotEmpty() && city.isNotEmpty() && selectedRoleId != -1) {
                if (password.length < 6) {
                    Toast.makeText(baseContext, "Password should be at least 6 characters.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val role = findViewById<RadioButton>(selectedRoleId).text.toString().lowercase()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser!!.uid
                            val user = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "phone" to phone,
                                "bloodGroup" to bloodGroup,
                                "city" to city,
                                "role" to role,
                                "isAvailable" to true,
                                "lastDonationDate" to Date()
                            )

                            db.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener { 
                                    Toast.makeText(baseContext, "Registration successful.", Toast.LENGTH_SHORT).show()
                                    when (role) {
                                        "donor" -> navigateTo(DonorDashboardActivity::class.java)
                                        "requester" -> navigateTo(RequesterDashboardActivity::class.java)
                                        "admin" -> navigateTo(AdminDashboardActivity::class.java)
                                        else -> Toast.makeText(baseContext, "Unknown role.", Toast.LENGTH_SHORT).show()
                                    }
                                 }
                                .addOnFailureListener { e ->
                                    Toast.makeText(baseContext, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(baseContext, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        loginPrompt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }
}