package com.example.campusfix.screens.dashboard

import com.example.campusfix.data.RepairRequest

interface DashboardContract {
    interface View {
        fun updateTitle(title: String)
        fun setReportIssueVisibility(isVisible: Boolean)
        fun showProfilePhoto(photoUrl: String?)
        fun submitRequestsList(requests: List<RepairRequest>)
        fun showStaffActionBottomSheet(request: RepairRequest)
        fun showReporterEditBottomSheet(request: RepairRequest)
        fun navigateToReportIssue(userId: String)
        fun navigateToProfile(userId: String, userName: String, userEmail: String, userRole: String)
        fun navigateToLogin()
    }
    interface Presenter {
        fun initDashboard(role: String, userId: String)
        fun loadProfilePhoto(userId: String)
        fun onReportIssueClicked(userId: String)
        fun onRequestClicked(request: RepairRequest)
        fun onProfileMenuItemClicked(title: String, userId: String, userName: String, userEmail: String, userRole: String)
        fun observeRequests(userId: String)
        fun refreshData(userId: String)
    }
}
