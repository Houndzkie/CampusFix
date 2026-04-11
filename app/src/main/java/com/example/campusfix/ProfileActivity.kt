package com.example.campusfix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ProfileActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_profile)

        val textviewUsername = findViewById<TextView>(R.id.textviewUsername)
        val textviewSchoolID = findViewById<TextView>(R.id.textviewSchoolID)
        val textviewEmail = findViewById<TextView>(R.id.textviewEmail)
        val textviewRole = findViewById<TextView>(R.id.textviewRole)
        val buttonBackToDashboard = findViewById<Button>(R.id.buttonBackToDashboard)

        val userId = intent.getStringExtra("USER_ID") ?: "Unknown ID"
        val userName = intent.getStringExtra("USER_NAME") ?: "Unknown Name"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "Unknown Email"
        val userRole = intent.getStringExtra("USER_ROLE") ?: "Unknown Role"

        textviewUsername.text = userName
        textviewSchoolID.text = userId
        textviewEmail.text = userEmail
        textviewRole.text = userRole

        // Navigation back to Dashboard
        buttonBackToDashboard.setOnClickListener {
            finish()
        }
    }
}