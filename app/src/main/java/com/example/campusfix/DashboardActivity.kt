package com.example.campusfix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DashboardActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val textviewWelcome = findViewById<TextView>(R.id.textviewWelcome)
        val buttonToProfile = findViewById<Button>(R.id.buttonToProfile)
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        val buttonReportIssue = findViewById<Button>(R.id.buttonReportIssue)

        textviewWelcome.text = "Welcome back, User!"

        buttonToProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        buttonLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonReportIssue.setOnClickListener {
        }
    }
}