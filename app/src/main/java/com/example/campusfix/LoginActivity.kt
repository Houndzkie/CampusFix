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
            val idInput = edittextLoginSchoolID.text.toString().trim()
            val passwordInput = edittextLoginPassword.text.toString()

            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

            val registeredPass = sharedPref.getString("${idInput}_password", null)
            val registeredRole = sharedPref.getString("${idInput}_role", "")

            var isValid = true

            if (idInput.isEmpty()) {
                edittextLoginSchoolID.error = "School ID is required"
                isValid = false
            }

            if (passwordInput.isEmpty()) {
                edittextLoginPassword.error = "Password is required"
                isValid = false
            }

            if (isValid) {
                if (registeredPass == null) {
                    edittextLoginSchoolID.error = "No account found with this ID"
                } else if (passwordInput == registeredPass) {
                    val fullName = sharedPref.getString("${idInput}_fullName", "User")
                    val emailAddress = sharedPref.getString("${idInput}_email", "Unknown Email")

                    val intent = Intent(this, UnifiedDashboardActivity::class.java)

                    intent.putExtra("USER_ROLE", registeredRole)
                    intent.putExtra("USER_NAME", fullName)
                    intent.putExtra("USER_ID", idInput)
                    intent.putExtra("USER_EMAIL", emailAddress)
                    startActivity(intent)

                } else {
                    edittextLoginPassword.error = "Invalid Password"
                }
            }
        }

        buttonCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}