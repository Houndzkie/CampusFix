package com.example.campusfix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ForgotPasswordActivity : Activity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_forgotpassword)

        val editTextForgotPasswordStudentID = findViewById<EditText>(R.id.edittextForgotPasswordStudentID)
        val edittextNewPassword = findViewById<EditText>(R.id.edittextNewPassword)
        val edittextConfirmNewPassword = findViewById<EditText>(R.id.edittextConfirmNewPassword)
        val buttonChangePassword = findViewById<Button>(R.id.buttonChangePassword)

        buttonChangePassword.setOnClickListener {
            // 1. Capture all inputs
            val inputID = editTextForgotPasswordStudentID.text.toString()
            val newPassInput = edittextNewPassword.text.toString()
            val confirmNewPassInput = edittextConfirmNewPassword.text.toString()

            // 2. Access SharedPreferences
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

            // Check if an account actually exists for this ID by looking for its saved role or password
            val savedRole = sharedPref.getString("${inputID}_role", null)

            var isValid = true

            // --- Check for Empty Fields ---
            if (inputID.isEmpty()) {
                editTextForgotPasswordStudentID.error = "Student ID is required"
                isValid = false
            }
            if (newPassInput.isEmpty()) {
                edittextNewPassword.error = "New password is required"
                isValid = false
            }
            if (confirmNewPassInput.isEmpty()) {
                edittextConfirmNewPassword.error = "Please confirm your new password"
                isValid = false
            }

            // --- Perform Logic Checks if fields aren't empty ---
            if (isValid) {
                // A. Verify if the account exists in the system
                if (savedRole == null) {
                    editTextForgotPasswordStudentID.error = "No account found with this ID"
                }
                // B. Verify New Password Length (Min 8 characters)
                else if (newPassInput.length < 8) {
                    edittextNewPassword.error = "New password must be at least 8 characters"
                }
                // C. Verify New Password and Confirm Match
                else if (newPassInput != confirmNewPassInput) {
                    edittextConfirmNewPassword.error = "New passwords do not match"
                }
                // D. Success - Update the "Database" for THIS specific ID
                else {
                    val editor = sharedPref.edit()
                    // We use the unique key format: "${inputID}_password"
                    editor.putString("${inputID}_password", newPassInput)
                    editor.apply()

                    // Return to Login
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
