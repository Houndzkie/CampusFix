package com.example.campusfix.screens.dashboard

import com.example.campusfix.data.DataManager
import com.example.campusfix.data.RepairRequest
import kotlinx.coroutines.flow.StateFlow

class DashboardModel {
    fun getProfilePhotoUrl(userId: String): String? {
        return DataManager.getProfilePhoto(userId)
    }

    fun getRequestsFlow(): StateFlow<List<RepairRequest>> {
        return DataManager.requestsFlow
    }

    fun getRequestsValue(): List<RepairRequest> {
        return DataManager.requestsFlow.value
    }
}
