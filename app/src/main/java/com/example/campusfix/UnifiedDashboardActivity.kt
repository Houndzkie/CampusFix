package com.example.campusfix

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class UnifiedDashboardActivity : AppCompatActivity() {

    private lateinit var adapter: RequestsAdapter
    private var isStaff = false
    private var currentUserId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unified_dashboard)

        DataManager.init(this)

        val userRole = intent.getStringExtra("USER_ROLE") ?: "Student"
        val userName = intent.getStringExtra("USER_NAME") ?: "User"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "Unknown Email"
        currentUserId = intent.getStringExtra("USER_ID") ?: ""

        isStaff = userRole == "Maintenance Staff"

        val textviewDashboardTitle = findViewById<TextView>(R.id.textviewDashboardTitle)
        val textviewWelcome = findViewById<TextView>(R.id.textviewWelcome)
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        val buttonReportIssue = findViewById<Button>(R.id.buttonReportIssue)
        val buttonToProfile = findViewById<Button>(R.id.buttonToProfile)
        val recyclerviewRequests = findViewById<RecyclerView>(R.id.recyclerviewRequests)

        val firstName = userName.trim().split(" ")[0]
        textviewWelcome.text = "Welcome back, $firstName!"

        if (isStaff) {
            textviewDashboardTitle.text = "Staff Dashboard"
            buttonReportIssue.visibility = View.GONE
        } else {
            textviewDashboardTitle.text = "Dashboard"
            buttonReportIssue.visibility = View.VISIBLE
        }

        adapter = RequestsAdapter { request ->
            if (isStaff) {
                val bottomSheet = StaffActionBottomSheet(request)
                bottomSheet.show(supportFragmentManager, "StaffActionBottomSheet")
            } else {
                val bottomSheet = ReporterEditBottomSheet(request)
                bottomSheet.show(supportFragmentManager, "ReporterEditBottomSheet")
            }
        }

        recyclerviewRequests.layoutManager = LinearLayoutManager(this)
        recyclerviewRequests.adapter = adapter

        lifecycleScope.launch {
            DataManager.requestsFlow.collect { requests ->
                val filteredList = if (isStaff) {
                    requests.filter { it.status == "Pending" || it.status == "In Progress" }
                } else {
                    if (currentUserId.isNotEmpty()) {
                        requests.filter { it.reporterId == currentUserId }
                    } else {
                        requests // fallback if no user id
                    }
                }
                adapter.submitList(filteredList)
            }
        }

        buttonReportIssue.setOnClickListener {
            val intentReport = Intent(this, ReportIssueActivity::class.java)
            intentReport.putExtra("USER_ID", currentUserId)
            startActivity(intentReport)
        }

        buttonToProfile.setOnClickListener {
            val intentProfile = Intent(this, ProfileActivity::class.java)
            intentProfile.putExtra("USER_ID", currentUserId)
            intentProfile.putExtra("USER_NAME", userName)
            intentProfile.putExtra("USER_EMAIL", userEmail)
            intentProfile.putExtra("USER_ROLE", userRole)
            startActivity(intentProfile)
        }

        buttonLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val requests = DataManager.requestsFlow.value
        val filteredList = if (isStaff) {
            requests.filter { it.status == "Pending" || it.status == "In Progress" }
        } else {
            if (currentUserId.isNotEmpty()) {
                requests.filter { it.reporterId == currentUserId }
            } else {
                requests // fallback if no user id
            }
        }
        adapter.submitList(filteredList)
    }
}
