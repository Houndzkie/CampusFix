package com.example.campusfix.screens.login

import android.content.Context
import android.content.SharedPreferences

class LoginModel(private val context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun getRegisteredPass(idInput: String): String? {
        return sharedPref.getString("${idInput}_password", null)
    }

    fun getRegisteredRole(idInput: String): String? {
        return sharedPref.getString("${idInput}_role", "")
    }

    fun getFullName(idInput: String): String? {
        return sharedPref.getString("${idInput}_fullName", "User")
    }

    fun getEmailAddress(idInput: String): String? {
        return sharedPref.getString("${idInput}_email", "Unknown Email")
    }
}
