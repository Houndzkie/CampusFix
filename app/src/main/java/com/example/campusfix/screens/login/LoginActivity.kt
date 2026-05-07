package com.example.campusfix.screens.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.campusfix.R
import com.example.campusfix.screens.forgotpassword.ForgotPasswordActivity
import com.example.campusfix.screens.register.RegisterActivity
import com.example.campusfix.screens.dashboard.UnifiedDashboardActivity

class LoginActivity : Activity(), LoginContract.View {
    private lateinit var loginPresenter: LoginPresenter
    private lateinit var edittextLoginSchoolID: EditText
    private lateinit var edittextLoginPassword: EditText

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_login)

        loginPresenter = LoginPresenter(this, LoginModel(this))

        edittextLoginSchoolID = findViewById(R.id.edittextLoginSchoolID)
        edittextLoginPassword = findViewById(R.id.edittextLoginPassword)
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

            loginPresenter.login(idInput, passwordInput)
        }

        buttonCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showSchoolIDError(message: String) {
        edittextLoginSchoolID.error = message
    }

    override fun showPasswordError(message: String) {
        edittextLoginPassword.error = message
    }

    override fun navigateToDashboard(role: String?, name: String?, id: String?, email: String?) {
        val intent = Intent(this, UnifiedDashboardActivity::class.java)
        intent.putExtra("USER_ROLE", role)
        intent.putExtra("USER_NAME", name)
        intent.putExtra("USER_ID", id)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
    }
}
