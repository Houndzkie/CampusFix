package com.example.campusfix.screens.report

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.campusfix.data.DataManager
import com.example.campusfix.R

class ReportIssueActivity : AppCompatActivity(), ReportContract.View {

    private lateinit var presenter: ReportPresenter
    private var tempCameraUri: Uri? = null

    private lateinit var edittextIssue: EditText
    private lateinit var edittextLocation: EditText
    private lateinit var edittextDescription: EditText
    private lateinit var buttonUploadPhoto: LinearLayout
    private lateinit var imageviewPhotoPreview: ImageView

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            presenter.onPhotoOptionSelected(0) // Launch camera if granted
        } else {
            Toast.makeText(this, "Camera permission is required to take photos.", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        presenter.onPhotoCaptured(success, tempCameraUri)
    }

    private val pickGalleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        presenter.onGalleryPhotoSelected(uri)
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_reportissue)
        DataManager.init(this)

        presenter = ReportPresenter(this, ReportModel(this))

        val buttonReportIssue = findViewById<Button>(R.id.buttonReportIssue)
        edittextIssue = findViewById(R.id.edittextIssue)
        edittextLocation = findViewById(R.id.edittextLocation)
        edittextDescription = findViewById(R.id.edittextDescription)
        buttonUploadPhoto = findViewById(R.id.buttonUploadPhoto)
        imageviewPhotoPreview = findViewById(R.id.imageviewPhotoPreview)
        
        val currentUserId = intent.getStringExtra("USER_ID") ?: ""

        buttonUploadPhoto.setOnClickListener { showPhotoOptions() }
        imageviewPhotoPreview.setOnClickListener { showPhotoOptions() }

        buttonReportIssue.setOnClickListener {
            val issue = edittextIssue.text.toString().trim()
            val location = edittextLocation.text.toString().trim()
            val description = edittextDescription.text.toString().trim()

            presenter.onSubmitClicked(issue, location, description, currentUserId)
        }
    }

    override fun showPhotoOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Add Photo")
            .setItems(options) { _, which ->
                presenter.onPhotoOptionSelected(which)
            }
            .show()
    }

    override fun launchCamera(tempUri: Uri) {
        tempCameraUri = tempUri
        if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            try {
                takePictureLauncher.launch(tempUri)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Camera app not found or could not be opened.", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    override fun launchGallery() {
        pickGalleryLauncher.launch(
            PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                .build()
        )
    }

    override fun showPhotoPreview(uri: Uri) {
        imageviewPhotoPreview.setImageURI(uri)
        imageviewPhotoPreview.visibility = View.VISIBLE
        buttonUploadPhoto.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun closeScreen() {
        finish()
    }
}
