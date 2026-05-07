package com.example.campusfix.screens.forgotpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.campusfix.R
import com.example.campusfix.screens.login.LoginActivity

class ForgotPasswordActivity : Activity(), ForgotPasswordContract.View {
    private lateinit var presenter: ForgotPasswordPresenter

    private lateinit var editTextForgotPasswordStudentID: EditText
    private lateinit var edittextNewPassword: EditText
    private lateinit var edittextConfirmNewPassword: EditText

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_forgotpassword)

        presenter = ForgotPasswordPresenter(this, ForgotPasswordModel(this))

        editTextForgotPasswordStudentID = findViewById(R.id.edittextForgotPasswordStudentID)
        edittextNewPassword = findViewById(R.id.edittextNewPassword)
        edittextConfirmNewPassword = findViewById(R.id.edittextConfirmNewPassword)
        val buttonChangePassword = findViewById<Button>(R.id.buttonChangePassword)

        buttonChangePassword.setOnClickListener {
            val inputID = editTextForgotPasswordStudentID.text.toString()
            val newPassInput = edittextNewPassword.text.toString()
            val confirmNewPassInput = edittextConfirmNewPassword.text.toString()

            presenter.changePassword(inputID, newPassInput, confirmNewPassInput)
        }
    }

    override fun showIDError(message: String) {
        editTextForgotPasswordStudentID.error = message
    }

    override fun showNewPasswordError(message: String) {
        edittextNewPassword.error = message
    }

    override fun showConfirmPasswordError(message: String) {
        edittextConfirmNewPassword.error = message
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
