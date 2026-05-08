package com.example.campusfix.screens.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.campusfix.data.DataManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class ProfileModel(private val context: Context) {
    fun getProfilePhotoUrl(userId: String): String? {
        return DataManager.getProfilePhoto(userId)
    }

    fun removeProfilePhoto(userId: String) {
        DataManager.saveProfilePhoto(userId, "")
    }

    fun createTempImageUri(): Uri {
        val imagesDir = File(context.cacheDir, "profile_images")
        if (!imagesDir.exists()) imagesDir.mkdirs()
        val imageFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
        return FileProvider.getUriForFile(context, "${context.applicationContext.packageName}.fileprovider", imageFile)
    }

    fun saveImageToInternalStorage(sourceUri: Uri, userId: String): Uri? {
        return try {
            try {
                context.contentResolver.takePersistableUriPermission(sourceUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: SecurityException) {
                // Ignore
            }

            val imagesDir = File(context.filesDir, "profile_images")
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
            
            DataManager.saveProfilePhoto(userId, savedUri.toString())
            savedUri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
