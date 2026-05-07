package com.example.campusfix.screens.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusfix.R
import com.example.campusfix.data.DataManager
import com.example.campusfix.data.RepairRequest
import com.example.campusfix.screens.login.LoginActivity
import com.example.campusfix.screens.profile.ProfileActivity
import com.example.campusfix.screens.report.ReportIssueActivity
import com.example.campusfix.ui.adapters.RequestsAdapter

class UnifiedDashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter
    private lateinit var adapter: RequestsAdapter
    private var currentUserId = ""
    private var userRole = ""
    private var userName = ""
    private var userEmail = ""

    private lateinit var textviewDashboardTitle: TextView
    private lateinit var imageviewProfileIcon: android.widget.ImageView
    private lateinit var buttonReportIssue: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
    private lateinit var recyclerviewRequests: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unified_dashboard)

        DataManager.init(this)

        userRole = intent.getStringExtra("USER_ROLE") ?: "Student"
        userName = intent.getStringExtra("USER_NAME") ?: "User"
        userEmail = intent.getStringExtra("USER_EMAIL") ?: "Unknown Email"
        currentUserId = intent.getStringExtra("USER_ID") ?: ""

        textviewDashboardTitle = findViewById(R.id.textviewDashboardTitle)
        imageviewProfileIcon = findViewById(R.id.imageviewProfileIcon)
        val textviewIdNumber = findViewById<TextView>(R.id.textviewIdNumber)
        buttonReportIssue = findViewById(R.id.buttonReportIssue)
        recyclerviewRequests = findViewById(R.id.recyclerviewRequests)

        textviewIdNumber.text = currentUserId

        presenter = DashboardPresenter(this, DashboardModel())
        presenter.initDashboard(userRole, currentUserId)

        adapter = RequestsAdapter { request ->
            presenter.onRequestClicked(request)
        }

        recyclerviewRequests.layoutManager = LinearLayoutManager(this)
        recyclerviewRequests.adapter = adapter

        buttonReportIssue.setOnClickListener {
            presenter.onReportIssueClicked(currentUserId)
        }

        imageviewProfileIcon.setOnClickListener { view ->
            val popupMenu = android.widget.PopupMenu(this, view)
            popupMenu.menu.add("View Profile")
            popupMenu.menu.add("Logout")
            popupMenu.setOnMenuItemClickListener { menuItem ->
                presenter.onProfileMenuItemClicked(
                    menuItem.title.toString(),
                    currentUserId,
                    userName,
                    userEmail,
                    userRole
                )
                true
            }
            popupMenu.show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshData(currentUserId)
    }

    override fun updateTitle(title: String) {
        textviewDashboardTitle.text = title
    }

    override fun setReportIssueVisibility(isVisible: Boolean) {
        if (isVisible) {
            buttonReportIssue.visibility = View.VISIBLE
        } else {
            buttonReportIssue.visibility = View.GONE
        }
    }

    override fun showProfilePhoto(photoUrl: String?) {
        if (!photoUrl.isNullOrEmpty()) {
            try {
                imageviewProfileIcon.setImageURI(android.net.Uri.parse(photoUrl))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun submitRequestsList(requests: List<RepairRequest>) {
        adapter.submitList(requests)
    }

    override fun showStaffActionBottomSheet(request: RepairRequest) {
        val bottomSheet = StaffActionBottomSheet(request)
        bottomSheet.show(supportFragmentManager, "StaffActionBottomSheet")
    }

    override fun showReporterEditBottomSheet(request: RepairRequest) {
        val bottomSheet = ReporterEditBottomSheet(request)
        bottomSheet.show(supportFragmentManager, "ReporterEditBottomSheet")
    }

    override fun navigateToReportIssue(userId: String) {
        val intentReport = Intent(this, ReportIssueActivity::class.java)
        intentReport.putExtra("USER_ID", userId)
        startActivity(intentReport)
    }

    override fun navigateToProfile(userId: String, userName: String, userEmail: String, userRole: String) {
        val intentProfile = Intent(this, ProfileActivity::class.java)
        intentProfile.putExtra("USER_ID", userId)
        intentProfile.putExtra("USER_NAME", userName)
        intentProfile.putExtra("USER_EMAIL", userEmail)
        intentProfile.putExtra("USER_ROLE", userRole)
        startActivity(intentProfile)
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
