package com.example.campusfix.screens.report

import android.net.Uri

interface ReportContract {
    interface View {
        fun showPhotoOptions()
        fun launchCamera(tempUri: Uri)
        fun launchGallery()
        fun showPhotoPreview(uri: Uri)
        fun showMessage(message: String)
        fun closeScreen()
    }
    
    interface Presenter {
        fun onPhotoOptionSelected(optionIndex: Int)
        fun onPhotoCaptured(success: Boolean, tempUri: Uri?)
        fun onGalleryPhotoSelected(uri: Uri?)
        fun onSubmitClicked(issue: String, location: String, description: String, userId: String)
    }
}
