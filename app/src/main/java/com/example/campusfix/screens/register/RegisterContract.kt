package com.example.campusfix.screens.register

interface RegisterContract {
    interface View {
        fun showFullNameError(message: String)
        fun showStudentIDError(message: String)
        fun showEmailError(message: String)
        fun showRoleError(message: String)
        fun showPasswordError(message: String)
        fun showConfirmPasswordError(message: String)
        fun navigateToLogin()
    }
    
    interface Presenter {
        fun register(
            fullName: String,
            studentID: String,
            email: String,
            password: String,
            confirmPassword: String,
            roleIndex: Int,
            selectedRole: String
        )
    }
}
