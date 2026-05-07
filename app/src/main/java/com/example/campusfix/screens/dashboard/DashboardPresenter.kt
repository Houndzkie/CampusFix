package com.example.campusfix.screens.dashboard

import com.example.campusfix.data.RepairRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardPresenter(
    val view: DashboardContract.View,
    val model: DashboardModel
) : DashboardContract.Presenter {
    
    private var isStaff = false
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun initDashboard(role: String, userId: String) {
        isStaff = role == "Maintenance Staff"
        if (isStaff) {
            view.updateTitle("Staff Dashboard")
            view.setReportIssueVisibility(false)
        } else {
            view.updateTitle("Dashboard")
            view.setReportIssueVisibility(true)
        }
        loadProfilePhoto(userId)
        observeRequests(userId)
    }

    override fun loadProfilePhoto(userId: String) {
        val existingPhotoUrl = model.getProfilePhotoUrl(userId)
        if (!existingPhotoUrl.isNullOrEmpty()) {
            view.showProfilePhoto(existingPhotoUrl)
        }
    }

    override fun observeRequests(userId: String) {
        coroutineScope.launch {
            model.getRequestsFlow().collect { requests ->
                filterAndSubmitRequests(requests, userId)
            }
        }
    }

    override fun refreshData(userId: String) {
        val requests = model.getRequestsValue()
        filterAndSubmitRequests(requests, userId)
        loadProfilePhoto(userId)
    }

    private fun filterAndSubmitRequests(requests: List<RepairRequest>, userId: String) {
        val filteredList = if (isStaff) {
            requests.filter { it.status == "Pending" || it.status == "In Progress" }
        } else {
            if (userId.isNotEmpty()) {
                requests.filter { it.reporterId == userId }
            } else {
                requests
            }
        }
        view.submitRequestsList(filteredList)
    }

    override fun onReportIssueClicked(userId: String) {
        view.navigateToReportIssue(userId)
    }

    override fun onRequestClicked(request: RepairRequest) {
        if (isStaff) {
            view.showStaffActionBottomSheet(request)
        } else {
            view.showReporterEditBottomSheet(request)
        }
    }

    override fun onProfileMenuItemClicked(title: String, userId: String, userName: String, userEmail: String, userRole: String) {
        when (title) {
            "View Profile" -> view.navigateToProfile(userId, userName, userEmail, userRole)
            "Logout" -> view.navigateToLogin()
        }
    }
}
