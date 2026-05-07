package com.example.campusfix.screens.forgotpassword

import android.content.Context
import android.content.SharedPreferences

class ForgotPasswordModel(private val context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun isAccountExists(inputID: String): Boolean {
        val savedRole = sharedPref.getString("${inputID}_role", null)
        return savedRole != null
    }

    fun updatePassword(inputID: String, newPassInput: String) {
        val editor = sharedPref.edit()
        editor.putString("${inputID}_password", newPassInput)
        editor.apply()
    }
}
