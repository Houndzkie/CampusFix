package com.example.campusfix.screens.report

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.campusfix.data.DataManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class ReportModel(private val context: Context) {
    var selectedPhotoUri: Uri? = null

    fun submitRequest(issue: String, location: String, description: String, userId: String): Boolean {
        if (issue.isNotEmpty() && location.isNotEmpty() && description.isNotEmpty() && selectedPhotoUri != null) {
            DataManager.addRequest(issue, location, description, selectedPhotoUri.toString(), userId)
            return true
        }
        return false
    }

    fun createTempImageUri(): Uri {
        val imagesDir = File(context.filesDir, "images")
        if (!imagesDir.exists()) imagesDir.mkdirs()
        val imageFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
        return FileProvider.getUriForFile(context, "${context.applicationContext.packageName}.fileprovider", imageFile)
    }

    fun saveImageToInternalStorage(sourceUri: Uri): Uri? {
        return try {
            try {
                context.contentResolver.takePersistableUriPermission(sourceUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: SecurityException) {
                // Ignore
            }

            val imagesDir = File(context.filesDir, "images")
            if (!imagesDir.exists()) imagesDir.mkdirs()
            val destFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
            
            val inputStream: InputStream? = context.contentResolver.openInputStream(sourceUri)
            val outputStream = FileOutputStream(destFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            
            val savedUri = FileProvider.getUriForFile(
                context,
                "${context.applicationContext.packageName}.fileprovider",
                destFile
            )
            selectedPhotoUri = savedUri
            savedUri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
