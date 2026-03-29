package com.example.campusfix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : Activity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_login)

        val edittextLoginSchoolID = findViewById<EditText>(R.id.edittextLoginSchoolID)
        val edittextLoginPassword = findViewById<EditText>(R.id.edittextLoginPassword)
        val textviewLoginForgotPassword = findViewById<TextView>(R.id.textviewLoginForgotPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonCreateAccount = findViewById<Button>(R.id.buttonLoginCreateAccount)

        textviewLoginForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val idInput = edittextLoginSchoolID.text.toString().trim() // Added .trim() to prevent space errors
            val passwordInput = edittextLoginPassword.text.toString()

            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

            // Look for the password and role using the dynamic key
            val registeredPass = sharedPref.getString("${idInput}_password", null)
            val registeredRole = sharedPref.getString("${idInput}_role", "")

            var isValid = true

            // 1. Check for Empty Fields
            if (idInput.isEmpty()) {
                edittextLoginSchoolID.error = "School ID is required"
                isValid = false
            }

            if (passwordInput.isEmpty()) {
                edittextLoginPassword.error = "Password is required"
                isValid = false
            }

            // 2. Only proceed if fields are not empty
            if (isValid) {
                if (registeredPass == null) {
                    // ID does not exist in SharedPreferences
                    edittextLoginSchoolID.error = "No account found with this ID"
                }
                else if (passwordInput == registeredPass) {
                    // SUCCESS: ID and Password match

                    val intent = when (registeredRole) {
                        "Maintenance Staff" -> Intent(this, MaintenanceDashboardActivity::class.java)
                        "Student", "Faculty" -> Intent(this, StudentDashboardActivity::class.java)
                        else -> Intent(this, StudentDashboardActivity::class.java)
                    }

                    intent.putExtra("USER_ROLE", registeredRole)
                    startActivity(intent)

                } else {
                    // ID exists but password is wrong
                    edittextLoginPassword.error = "Invalid Password"
                }
            }
        }

        buttonCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Inside onCreate
        val buttonDebugClearData = findViewById<Button>(R.id.buttonDebugClearData)

        buttonDebugClearData.setOnClickListener {
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPref.edit()

            // This wipes the entire XML file clean
            editor.clear()
            editor.apply()

            // Feedback so you know it worked
            android.widget.Toast.makeText(this, "Database Cleared!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}