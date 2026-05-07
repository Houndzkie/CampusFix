package com.example.campusfix.screens.report

import android.net.Uri

class ReportPresenter(
    val view: ReportContract.View,
    val model: ReportModel
) : ReportContract.Presenter {

    override fun onPhotoOptionSelected(optionIndex: Int) {
        when (optionIndex) {
            0 -> {
                val tempUri = model.createTempImageUri()
                view.launchCamera(tempUri)
            }
            1 -> view.launchGallery()
        }
    }

    override fun onPhotoCaptured(success: Boolean, tempUri: Uri?) {
        if (success && tempUri != null) {
            val savedUri = model.saveImageToInternalStorage(tempUri)
            if (savedUri != null) {
                view.showPhotoPreview(savedUri)
            } else {
                view.showMessage("Failed to process image.")
            }
        }
    }

    override fun onGalleryPhotoSelected(uri: Uri?) {
        if (uri != null) {
            val savedUri = model.saveImageToInternalStorage(uri)
            if (savedUri != null) {
                view.showPhotoPreview(savedUri)
            } else {
                view.showMessage("Failed to process image.")
            }
        }
    }

    override fun onSubmitClicked(issue: String, location: String, description: String, userId: String) {
        val success = model.submitRequest(issue, location, description, userId)
        if (success) {
            view.closeScreen()
        } else {
            view.showMessage("Please fill in all details and attach a photo.")
        }
    }
}
