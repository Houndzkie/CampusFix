package com.example.campusfix.screens.register

class RegisterPresenter(
    val view: RegisterContract.View,
    val model: RegisterModel
) : RegisterContract.Presenter {

    override fun register(
        fullName: String,
        studentID: String,
        email: String,
        password: String,
        confirmPassword: String,
        roleIndex: Int,
        selectedRole: String
    ) {
        var isValid = true

        if (fullName.isEmpty()) {
            view.showFullNameError("Full Name is required")
            isValid = false
        }
        if (studentID.isEmpty()) {
            view.showStudentIDError("Student ID is required")
            isValid = false
        } else if (model.isIDExists(studentID)) {
            view.showStudentIDError("This ID is already registered")
            isValid = false
        }
        if (email.isEmpty()) {
            view.showEmailError("Institutional Email is required")
            isValid = false
        }
        if (roleIndex == 0) {
            view.showRoleError("Please select a role")
            isValid = false
        }
        if (password.length < 8) {
            view.showPasswordError("Password must be at least 8 characters")
            isValid = false
        }
        if (confirmPassword != password) {
            view.showConfirmPasswordError("Passwords do not match")
            isValid = false
        }

        if (isValid) {
            model.saveUser(studentID, fullName, email, selectedRole, password)
            view.navigateToLogin()
        }
    }
}
