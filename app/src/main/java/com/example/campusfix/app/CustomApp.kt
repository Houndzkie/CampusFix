package com.example.campusfix.app

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.campusfix.data.User

class CustomApp : Application() {
    val id: String = "22-1004-886"
    val password: String = "1234"

    var loginUser = User()

    override fun onCreate() {
        super.onCreate()
        Log.e("Custom App", "onCreate is called!")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
