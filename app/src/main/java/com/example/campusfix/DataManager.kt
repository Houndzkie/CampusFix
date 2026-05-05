package com.example.campusfix

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

object DataManager {
    private const val PREFS_NAME = "campusfix_data"
    private const val REQUESTS_KEY = "repair_requests"
    
    private val gson = Gson()
    private val _requestsFlow = MutableStateFlow<List<RepairRequest>>(emptyList())
    val requestsFlow: StateFlow<List<RepairRequest>> = _requestsFlow
    
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            loadData()
        }
    }

    private fun loadData() {
        val json = sharedPreferences?.getString(REQUESTS_KEY, null)
        val type = object : TypeToken<List<RepairRequest>>() {}.type
        val requests: List<RepairRequest> = if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
        _requestsFlow.value = requests
        saveData(requests)
    }

    private fun saveData(requests: List<RepairRequest>) {
        val json = gson.toJson(requests)
        sharedPreferences?.edit()?.putString(REQUESTS_KEY, json)?.apply()
    }

    fun addRequest(issue: String, location: String, description: String, photoUrl: String, reporterId: String) {
        val currentList = _requestsFlow.value.toMutableList()
        val newRequest = RepairRequest(
            id = UUID.randomUUID().toString(),
            issue = issue,
            location = location,
            description = description,
            photoUrl = photoUrl,
            status = "Pending",
            reporterId = reporterId,
            timestamp = System.currentTimeMillis()
        )
        currentList.add(newRequest)
        _requestsFlow.value = currentList
        saveData(currentList)
    }

    fun updateRequestStatus(requestId: String, newStatus: String) {
        val currentList = _requestsFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == requestId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(status = newStatus)
            _requestsFlow.value = currentList
            saveData(currentList)
        }
    }

    fun updateRequestDetails(requestId: String, location: String, description: String) {
        val currentList = _requestsFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == requestId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(location = location, description = description)
            _requestsFlow.value = currentList
            saveData(currentList)
        }
    }

    fun saveProfilePhoto(userId: String, photoUrl: String) {
        sharedPreferences?.edit()?.putString("profile_photo_$userId", photoUrl)?.apply()
    }

    fun getProfilePhoto(userId: String): String? {
        return sharedPreferences?.getString("profile_photo_$userId", null)
    }
}
