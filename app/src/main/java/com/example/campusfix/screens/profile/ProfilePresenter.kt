package com.example.campusfix.screens.profile

import android.net.Uri

class ProfilePresenter(
    val view: ProfileContract.View,
    val model: ProfileModel
) : ProfileContract.Presenter {

    override fun loadProfileData(userId: String, userName: String, userEmail: String, userRole: String) {
        view.displayUserInfo(userName, userId, userEmail, userRole)

        val existingPhotoUrl = model.getProfilePhotoUrl(userId)
        if (!existingPhotoUrl.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(existingPhotoUrl)
                view.displayProfilePhoto(uri)
                view.setHasPhotoUI(true)
            } catch (e: Exception) {
                e.printStackTrace()
                view.setHasPhotoUI(false)
            }
        } else {
            view.setHasPhotoUI(false)
        }
    }

    override fun onAddPhotoClicked() {
        view.showPhotoOptions()
    }

    override fun onChangePhotoClicked() {
        view.showPhotoOptions()
    }

    override fun onRemovePhotoClicked(userId: String) {
        model.removeProfilePhoto(userId)
        view.displayDefaultPhoto()
        view.setHasPhotoUI(false)
        view.showMessage("Profile photo removed")
    }

    override fun onPhotoOptionSelected(optionIndex: Int) {
        when (optionIndex) {
            0 -> {
                val tempUri = model.createTempImageUri()
                view.launchCamera(tempUri)
            }
            1 -> view.launchGallery()
        }
    }

    override fun onPhotoCaptured(success: Boolean, tempUri: Uri?, userId: String) {
        if (success && tempUri != null) {
            val savedUri = model.saveImageToInternalStorage(tempUri, userId)
            if (savedUri != null) {
                view.displayProfilePhoto(savedUri)
                view.setHasPhotoUI(true)
                view.showMessage("Profile photo updated")
            } else {
                view.showMessage("Failed to process image.")
            }
        }
    }

    override fun onGalleryPhotoSelected(uri: Uri?, userId: String) {
        if (uri != null) {
            val savedUri = model.saveImageToInternalStorage(uri, userId)
            if (savedUri != null) {
                view.displayProfilePhoto(savedUri)
                view.setHasPhotoUI(true)
                view.showMessage("Profile photo updated")
            } else {
                view.showMessage("Failed to process image.")
            }
        }
    }

    override fun onBackClicked() {
        view.closeScreen()
    }
}
