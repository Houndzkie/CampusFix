package com.example.campusfix.screens.forgotpassword

class ForgotPasswordPresenter(
    val view: ForgotPasswordContract.View,
    val model: ForgotPasswordModel
) : ForgotPasswordContract.Presenter {

    override fun changePassword(inputID: String, newPassInput: String, confirmNewPassInput: String) {
        var isValid = true

        if (inputID.isEmpty()) {
            view.showIDError("Student ID is required")
            isValid = false
        }
        if (newPassInput.isEmpty()) {
            view.showNewPasswordError("New password is required")
            isValid = false
        }
        if (confirmNewPassInput.isEmpty()) {
            view.showConfirmPasswordError("Please confirm your new password")
            isValid = false
        }

        if (isValid) {
            if (!model.isAccountExists(inputID)) {
                view.showIDError("No account found with this ID")
            } else if (newPassInput.length < 8) {
                view.showNewPasswordError("New password must be at least 8 characters")
            } else if (newPassInput != confirmNewPassInput) {
                view.showConfirmPasswordError("New passwords do not match")
            } else {
                model.updatePassword(inputID, newPassInput)
                view.navigateToLogin()
            }
        }
    }
}
