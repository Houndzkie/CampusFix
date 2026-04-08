package com.example.campusfix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class RegisterActivity : Activity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_register)

        val edittextRegisterFullName = findViewById<EditText>(R.id.edittextRegisterFullName)
        val edittextRegisterStudentID = findViewById<EditText>(R.id.edittextRegisterStudentID)
        val edittextRegisterInstitutionalEmail = findViewById<EditText>(R.id.edittextRegisterInstitutionalEmail)
        val spinnerRegisterRole = findViewById<Spinner>(R.id.spinnerRegisterRole)
        val edittextRegisterPassword = findViewById<EditText>(R.id.edittextRegisterPassword)
        val edittextRegisterConfirmPassword = findViewById<EditText>(R.id.edittextRegisterConfirmPassword)
        val buttonCreateAccount = findViewById<Button>(R.id.buttonRegisterCreateAccount)

        val roles = listOf("Select Role", "Student", "Faculty", "Maintenance Staff")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRegisterRole.adapter = adapter

        buttonCreateAccount.setOnClickListener {
            // 1. Capture all inputs as defined in CampusFix specs
            val fullName = edittextRegisterFullName.text.toString()
            val studentID = edittextRegisterStudentID.text.toString()
            val email = edittextRegisterInstitutionalEmail.text.toString()
            val password = edittextRegisterPassword.text.toString()
            val confirmPassword = edittextRegisterConfirmPassword.text.toString()
            val roleIndex = spinnerRegisterRole.selectedItemPosition
            val selectedRole = spinnerRegisterRole.selectedItem.toString()

            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val existingID = sharedPref.getString("savedID", "")

            var isValid = true

            if (fullName.isEmpty()) {
                edittextRegisterFullName.error = "Full Name is required"
                isValid = false
            }
            if (studentID.isEmpty()) {
                edittextRegisterStudentID.error = "Student ID is required"
                isValid = false
            } else if (studentID == existingID) {
                edittextRegisterStudentID.error = "This ID is already registered"
                isValid = false
            }
            if (email.isEmpty()) {
                edittextRegisterInstitutionalEmail.error = "Institutional Email is required"
                isValid = false
            }
            if (roleIndex == 0) {
                isValid = false
            }
            if (password.length < 8) {
                edittextRegisterPassword.error = "Password must be at least 8 characters"
                isValid = false
            }
            if (confirmPassword != password) {
                edittextRegisterConfirmPassword.error = "Passwords do not match"
                isValid = false
            }

            if (isValid) {
                val editor = sharedPref.edit()

                editor.putString("${studentID}_fullName", fullName)
                editor.putString("${studentID}_email", email)
                editor.putString("${studentID}_role", selectedRole)
                editor.putString("${studentID}_password", password)

                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}