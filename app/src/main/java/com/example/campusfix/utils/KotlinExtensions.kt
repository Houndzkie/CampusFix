package com.example.campusfix.utils

import android.app.Activity
import android.widget.EditText
import android.widget.Toast

fun Activity.showToast(message: String) {
    return Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.getEditTextValue(id: Int) = findViewById<EditText>(id).text.toString()
