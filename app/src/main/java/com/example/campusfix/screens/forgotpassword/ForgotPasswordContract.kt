package com.example.campusfix.screens.forgotpassword

interface ForgotPasswordContract {
    interface View {
        fun showIDError(message: String)
        fun showNewPasswordError(message: String)
        fun showConfirmPasswordError(message: String)
        fun navigateToLogin()
    }
    
    interface Presenter {
        fun changePassword(inputID: String, newPassInput: String, confirmNewPassInput: String)
    }
}
