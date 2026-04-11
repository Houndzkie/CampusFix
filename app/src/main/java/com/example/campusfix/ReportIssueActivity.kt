package com.example.campusfix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

import android.widget.EditText

class ReportIssueActivity : Activity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_reportissue)

        DataManager.init(this)

        val buttonSubmitRequest = findViewById<Button>(R.id.buttonSubmitRequest)
        val edittextIssue = findViewById<EditText>(R.id.edittextIssue)
        val edittextLocation = findViewById<EditText>(R.id.edittextLocation)
        val edittextDescription = findViewById<EditText>(R.id.edittextDescription)
        
        val currentUserId = intent.getStringExtra("USER_ID") ?: ""

        buttonSubmitRequest.setOnClickListener {
            val issue = edittextIssue.text.toString().trim()
            val location = edittextLocation.text.toString().trim()
            val description = edittextDescription.text.toString().trim()

            if (issue.isNotEmpty() && location.isNotEmpty() && description.isNotEmpty()) {
                DataManager.addRequest(issue, location, description, "", currentUserId)
                finish()
            } else {
                android.widget.Toast.makeText(this@ReportIssueActivity, "Please fill in all details.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}
