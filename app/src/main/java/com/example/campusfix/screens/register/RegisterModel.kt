package com.example.campusfix.screens.register

import android.content.Context
import android.content.SharedPreferences

class RegisterModel(private val context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun isIDExists(studentID: String): Boolean {
        val existingID = sharedPref.getString("savedID", "")
        return studentID == existingID
    }

    fun saveUser(studentID: String, fullName: String, email: String, selectedRole: String, password: String) {
        val editor = sharedPref.edit()
        editor.putString("${studentID}_fullName", fullName)
        editor.putString("${studentID}_email", email)
        editor.putString("${studentID}_role", selectedRole)
        editor.putString("${studentID}_password", password)
        editor.putString("savedID", studentID)
        editor.apply()
    }
}
