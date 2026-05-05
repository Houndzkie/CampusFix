package com.example.campusfix

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.PickVisualMediaRequest
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class ProfileActivity : AppCompatActivity() {

    private var currentUserId = ""
    private var tempCameraUri: Uri? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            tempCameraUri?.let { uri ->
                saveUriToInternalStorageAndSet(uri)
            }
        }
    }

    private val pickGalleryLauncher = registerForActivityResult(PickVisualMedia()) { uri: Uri? ->
        uri?.let { saveUriToInternalStorageAndSet(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_profile)
        DataManager.init(this)

        val textviewUsername = findViewById<TextView>(R.id.textviewUsername)
        val textviewSchoolID = findViewById<TextView>(R.id.textviewSchoolID)
        val textviewEmail = findViewById<TextView>(R.id.textviewEmail)
        val textviewRole = findViewById<TextView>(R.id.textviewRole)
        val buttonBackToDashboard = findViewById<Button>(R.id.buttonBackToDashboard)
        val buttonChangePhoto = findViewById<Button>(R.id.buttonChangePhoto)
        val imageviewProfileLarge = findViewById<ImageView>(R.id.imageviewProfileLarge)

        currentUserId = intent.getStringExtra("USER_ID") ?: "Unknown ID"
        val userName = intent.getStringExtra("USER_NAME") ?: "Unknown Name"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "Unknown Email"
        val userRole = intent.getStringExtra("USER_ROLE") ?: "Unknown Role"

        textviewUsername.text = userName
        textviewSchoolID.text = currentUserId
        textviewEmail.text = userEmail
        textviewRole.text = userRole

        // Load existing photo
        val existingPhotoUrl = DataManager.getProfilePhoto(currentUserId)
        if (!existingPhotoUrl.isNullOrEmpty()) {
            try {
                imageviewProfileLarge.setImageURI(Uri.parse(existingPhotoUrl))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        buttonChangePhoto.setOnClickListener {
            showPhotoOptions()
        }

        // Navigation back to Dashboard
        buttonBackToDashboard.setOnClickListener {
            finish()
        }
    }

    private fun showPhotoOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Change Profile Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> launchCamera()
                    1 -> pickGalleryLauncher.launch(
                        PickVisualMediaRequest.Builder()
                            .setMediaType(PickVisualMedia.ImageOnly)
                            .build()
                    )
                }
            }
            .show()
    }

    private fun launchCamera() {
        val imagesDir = File(filesDir, "profile_images")
        if (!imagesDir.exists()) imagesDir.mkdirs()
        val imageFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
        tempCameraUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", imageFile)
        takePictureLauncher.launch(tempCameraUri!!)
    }

    private fun saveUriToInternalStorageAndSet(sourceUri: Uri) {
        try {
            val imagesDir = File(filesDir, "profile_images")
            if (!imagesDir.exists()) imagesDir.mkdirs()
            val destFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
            
            val inputStream: InputStream? = contentResolver.openInputStream(sourceUri)
            val outputStream = FileOutputStream(destFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            
            val savedUri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                destFile
            )
            
            val imageviewProfileLarge = findViewById<ImageView>(R.id.imageviewProfileLarge)
            imageviewProfileLarge.setImageURI(savedUri)
            
            DataManager.saveProfilePhoto(currentUserId, savedUri.toString())
            Toast.makeText(this, "Profile photo updated", Toast.LENGTH_SHORT).show()
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to process image.", Toast.LENGTH_SHORT).show()
        }
    }
}