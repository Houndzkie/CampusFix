package com.example.campusfix.screens.profile

import android.net.Uri

interface ProfileContract {
    interface View {
        fun displayUserInfo(userName: String, userId: String, userEmail: String, userRole: String)
        fun displayProfilePhoto(photoUri: Uri)
        fun displayDefaultPhoto()
        fun setHasPhotoUI(hasPhoto: Boolean)
        fun showMessage(message: String)
        fun launchCamera(tempUri: Uri)
        fun launchGallery()
        fun showPhotoOptions()
        fun closeScreen()
    }

    interface Presenter {
        fun loadProfileData(userId: String, userName: String, userEmail: String, userRole: String)
        fun onAddPhotoClicked()
        fun onChangePhotoClicked()
        fun onRemovePhotoClicked(userId: String)
        fun onPhotoOptionSelected(optionIndex: Int)
        fun onPhotoCaptured(success: Boolean, tempUri: Uri?, userId: String)
        fun onGalleryPhotoSelected(uri: Uri?, userId: String)
        fun onBackClicked()
    }
}
