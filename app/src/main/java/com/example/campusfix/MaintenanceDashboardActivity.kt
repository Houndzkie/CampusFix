package com.example.campusfix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MaintenanceDashboardActivity : Activity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_maintenancedashboard)

        val textviewWelcome = findViewById<TextView>(R.id.textviewWelcome)
        val buttonToProfile = findViewById<Button>(R.id.buttonToProfile)
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        val buttonViewActiveRepairs = findViewById<Button>(R.id.buttonViewActiveRepairs)

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

        buttonViewActiveRepairs.setOnClickListener {
            val intent = Intent(this, ActiveRepairsActivity::class.java)
            startActivity(intent)
        }
    }
}
