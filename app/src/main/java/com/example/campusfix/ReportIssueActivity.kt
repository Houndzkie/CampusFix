package com.example.campusfix

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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

class ReportIssueActivity : AppCompatActivity() {

    private var selectedPhotoUri: Uri? = null
    private var tempCameraUri: Uri? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            tempCameraUri?.let { uri ->
                saveUriToInternalStorageAndPreview(uri)
            }
        }
    }

    private val pickGalleryLauncher = registerForActivityResult(PickVisualMedia()) { uri: Uri? ->
        uri?.let { 
            try {
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            saveUriToInternalStorageAndPreview(it) 
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_reportissue)

        DataManager.init(this)

        val buttonSubmitRequest = findViewById<Button>(R.id.buttonSubmitRequest)
        val edittextIssue = findViewById<EditText>(R.id.edittextIssue)
        val edittextLocation = findViewById<EditText>(R.id.edittextLocation)
        val edittextDescription = findViewById<EditText>(R.id.edittextDescription)
        val buttonUploadPhoto = findViewById<LinearLayout>(R.id.buttonUploadPhoto)
        val imageviewPhotoPreview = findViewById<ImageView>(R.id.imageviewPhotoPreview)
        
        val currentUserId = intent.getStringExtra("USER_ID") ?: ""

        buttonUploadPhoto.setOnClickListener { showPhotoOptions() }
        imageviewPhotoPreview.setOnClickListener { showPhotoOptions() }

        buttonSubmitRequest.setOnClickListener {
            val issue = edittextIssue.text.toString().trim()
            val location = edittextLocation.text.toString().trim()
            val description = edittextDescription.text.toString().trim()

            if (issue.isNotEmpty() && location.isNotEmpty() && description.isNotEmpty() && selectedPhotoUri != null) {
                DataManager.addRequest(issue, location, description, selectedPhotoUri.toString(), currentUserId)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all details and attach a photo.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPhotoOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Add Photo")
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
        val imagesDir = File(filesDir, "images")
        if (!imagesDir.exists()) imagesDir.mkdirs()
        val imageFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
        tempCameraUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", imageFile)
        takePictureLauncher.launch(tempCameraUri!!)
    }

    private fun saveUriToInternalStorageAndPreview(sourceUri: Uri) {
        try {
            val imagesDir = File(filesDir, "images")
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
            selectedPhotoUri = savedUri
            
            val imageviewPhotoPreview = findViewById<ImageView>(R.id.imageviewPhotoPreview)
            val buttonUploadPhoto = findViewById<LinearLayout>(R.id.buttonUploadPhoto)
            
            imageviewPhotoPreview.setImageURI(savedUri)
            imageviewPhotoPreview.visibility = View.VISIBLE
            buttonUploadPhoto.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to process image.", Toast.LENGTH_SHORT).show()
        }
    }
}
