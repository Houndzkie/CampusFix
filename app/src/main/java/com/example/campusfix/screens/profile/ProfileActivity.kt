package com.example.campusfix.screens.profile

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.campusfix.data.DataManager
import com.example.campusfix.R

class ProfileActivity : AppCompatActivity(), ProfileContract.View {

    private lateinit var presenter: ProfilePresenter
    private var currentUserId = ""
    private var tempCameraUri: Uri? = null

    private lateinit var textviewUsername: TextView
    private lateinit var textviewSchoolID: TextView
    private lateinit var textviewEmail: TextView
    private lateinit var textviewRole: TextView
    private lateinit var buttonBackToDashboard: Button
    private lateinit var buttonAddPhoto: Button
    private lateinit var buttonChangePhoto: Button
    private lateinit var buttonRemovePhoto: Button
    private lateinit var layoutPhotoActions: android.view.View
    private lateinit var imageviewProfileLarge: ImageView

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        presenter.onPhotoCaptured(success, tempCameraUri, currentUserId)
    }

    private val pickGalleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        presenter.onGalleryPhotoSelected(uri, currentUserId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tempCameraUri?.let { outState.putParcelable("tempCameraUri", it) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        @Suppress("DEPRECATION")
        tempCameraUri = savedInstanceState.getParcelable("tempCameraUri") as Uri?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_profile)
        DataManager.init(this)

        presenter = ProfilePresenter(this, ProfileModel(this))

        textviewUsername = findViewById(R.id.textviewUsername)
        textviewSchoolID = findViewById(R.id.textviewSchoolID)
        textviewEmail = findViewById(R.id.textviewEmail)
        textviewRole = findViewById(R.id.textviewRole)
        buttonBackToDashboard = findViewById(R.id.buttonBackToDashboard)
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto)
        buttonChangePhoto = findViewById(R.id.buttonChangePhoto)
        buttonRemovePhoto = findViewById(R.id.buttonRemovePhoto)
        layoutPhotoActions = findViewById(R.id.layoutPhotoActions)
        imageviewProfileLarge = findViewById(R.id.imageviewProfileLarge)

        currentUserId = intent.getStringExtra("USER_ID") ?: "Unknown ID"
        val userName = intent.getStringExtra("USER_NAME") ?: "Unknown Name"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "Unknown Email"
        val userRole = intent.getStringExtra("USER_ROLE") ?: "Unknown Role"

        presenter.loadProfileData(currentUserId, userName, userEmail, userRole)

        buttonAddPhoto.setOnClickListener { presenter.onAddPhotoClicked() }
        buttonChangePhoto.setOnClickListener { presenter.onChangePhotoClicked() }
        buttonRemovePhoto.setOnClickListener { presenter.onRemovePhotoClicked(currentUserId) }
        buttonBackToDashboard.setOnClickListener { presenter.onBackClicked() }
    }

    override fun displayUserInfo(userName: String, userId: String, userEmail: String, userRole: String) {
        textviewUsername.text = userName
        textviewSchoolID.text = userId
        textviewEmail.text = userEmail
        textviewRole.text = userRole
    }

    override fun displayProfilePhoto(photoUri: Uri) {
        imageviewProfileLarge.setImageURI(photoUri)
    }

    override fun displayDefaultPhoto() {
        imageviewProfileLarge.setImageResource(R.drawable.anonymous)
    }

    override fun setHasPhotoUI(hasPhoto: Boolean) {
        if (hasPhoto) {
            buttonAddPhoto.visibility = android.view.View.GONE
            layoutPhotoActions.visibility = android.view.View.VISIBLE
        } else {
            buttonAddPhoto.visibility = android.view.View.VISIBLE
            layoutPhotoActions.visibility = android.view.View.GONE
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showPhotoOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Change Profile Photo")
            .setItems(options) { _, which ->
                presenter.onPhotoOptionSelected(which)
            }
            .show()
    }

    override fun launchCamera(tempUri: Uri) {
        tempCameraUri = tempUri
        takePictureLauncher.launch(tempUri)
    }

    override fun launchGallery() {
        pickGalleryLauncher.launch(
            PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                .build()
        )
    }

    override fun closeScreen() {
        finish()
    }
}
