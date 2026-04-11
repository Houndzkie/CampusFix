package com.example.campusfix

data class RepairRequest(
    val id: String,
    val issue: String,
    val location: String,
    val description: String,
    val photoUrl: String,
    val status: String, // "Pending", "In Progress", "Completed"
    val reporterId: String,
    val timestamp: Long
)
