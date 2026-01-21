package com.example.bloodlink

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodlink.data.BloodRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateRequestActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_request)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val bloodGroupEditText = findViewById<EditText>(R.id.bloodGroup)
        val cityEditText = findViewById<EditText>(R.id.city)
        val hospitalEditText = findViewById<EditText>(R.id.hospital)
        val contactNumberEditText = findViewById<EditText>(R.id.contactNumber)
        val urgencyEditText = findViewById<EditText>(R.id.urgency)
        val submitRequestButton = findViewById<Button>(R.id.submit_request_button)

        submitRequestButton.setOnClickListener {
            val bloodGroup = bloodGroupEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val hospital = hospitalEditText.text.toString().trim()
            val contactNumber = contactNumberEditText.text.toString().trim()
            val urgency = urgencyEditText.text.toString().trim()

            if (bloodGroup.isNotEmpty() && city.isNotEmpty() && hospital.isNotEmpty() && contactNumber.isNotEmpty() && urgency.isNotEmpty()) {
                val userId = auth.currentUser!!.uid
                val request = BloodRequest(
                    bloodGroup = bloodGroup,
                    city = city,
                    hospital = hospital,
                    contactNumber = contactNumber,
                    urgency = urgency,
                    createdBy = userId
                )

                db.collection("requests").add(request)
                    .addOnSuccessListener {
                        Toast.makeText(baseContext, "Request submitted successfully.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(baseContext, "Error submitting request: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(baseContext, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}