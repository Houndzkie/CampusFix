package com.example.campusfix.screens.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.campusfix.R
import com.example.campusfix.screens.login.LoginActivity

class RegisterActivity : Activity(), RegisterContract.View {
    
    private lateinit var presenter: RegisterPresenter
    
    private lateinit var edittextRegisterFullName: EditText
    private lateinit var edittextRegisterStudentID: EditText
    private lateinit var edittextRegisterInstitutionalEmail: EditText
    private lateinit var spinnerRegisterRole: Spinner
    private lateinit var edittextRegisterPassword: EditText
    private lateinit var edittextRegisterConfirmPassword: EditText

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenter(this, RegisterModel(this))

        edittextRegisterFullName = findViewById(R.id.edittextRegisterFullName)
        edittextRegisterStudentID = findViewById(R.id.edittextRegisterStudentID)
        edittextRegisterInstitutionalEmail = findViewById(R.id.edittextRegisterInstitutionalEmail)
        spinnerRegisterRole = findViewById(R.id.spinnerRegisterRole)
        edittextRegisterPassword = findViewById(R.id.edittextRegisterPassword)
        edittextRegisterConfirmPassword = findViewById(R.id.edittextRegisterConfirmPassword)
        val buttonCreateAccount = findViewById<Button>(R.id.buttonRegisterCreateAccount)

        val roles = listOf("Select Role", "Student", "Faculty", "Maintenance Staff")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRegisterRole.adapter = adapter

        buttonCreateAccount.setOnClickListener {
            val fullName = edittextRegisterFullName.text.toString()
            val studentID = edittextRegisterStudentID.text.toString()
            val email = edittextRegisterInstitutionalEmail.text.toString()
            val password = edittextRegisterPassword.text.toString()
            val confirmPassword = edittextRegisterConfirmPassword.text.toString()
            val roleIndex = spinnerRegisterRole.selectedItemPosition
            val selectedRole = spinnerRegisterRole.selectedItem.toString()

            presenter.register(fullName, studentID, email, password, confirmPassword, roleIndex, selectedRole)
        }
    }

    override fun showFullNameError(message: String) {
        edittextRegisterFullName.error = message
    }

    override fun showStudentIDError(message: String) {
        edittextRegisterStudentID.error = message
    }

    override fun showEmailError(message: String) {
        edittextRegisterInstitutionalEmail.error = message
    }

    override fun showRoleError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showPasswordError(message: String) {
        edittextRegisterPassword.error = message
    }

    override fun showConfirmPasswordError(message: String) {
        edittextRegisterConfirmPassword.error = message
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
