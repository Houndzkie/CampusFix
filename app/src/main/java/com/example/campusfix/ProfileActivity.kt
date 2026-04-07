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
        val textviewFirstName = findViewById<TextView>(R.id.textviewFirstName)
        val textviewMiddleName = findViewById<TextView>(R.id.textviewMiddleName)
        val textviewLastName = findViewById<TextView>(R.id.textviewLastName)
        val textviewEmail = findViewById<TextView>(R.id.textviewEmail)
        val buttonBackToDashboard = findViewById<Button>(R.id.buttonBackToDashboard)

        textviewUsername.text = "helge_justine"
        textviewFirstName.text = "Helge Justine"
        textviewMiddleName.text = "O."
        textviewLastName.text = "Dano"
        textviewEmail.text = "helgejustine.dano@cit.edu"

        // Navigation back to Dashboard
        buttonBackToDashboard.setOnClickListener {
            val intent = Intent(this, StudentDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}