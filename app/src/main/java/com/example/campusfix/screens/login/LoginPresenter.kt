package com.example.campusfix.screens.login

class LoginPresenter(
    val view: LoginContract.View,
    val model: LoginModel
) : LoginContract.Presenter {

    override fun login(idInput: String, passwordInput: String) {
        var isValid = true

        if (idInput.isEmpty()) {
            view.showSchoolIDError("School ID is required")
            isValid = false
        }

        if (passwordInput.isEmpty()) {
            view.showPasswordError("Password is required")
            isValid = false
        }

        if (isValid) {
            val registeredPass = model.getRegisteredPass(idInput)
            if (registeredPass == null) {
                view.showSchoolIDError("No account found with this ID")
            } else if (passwordInput == registeredPass) {
                val registeredRole = model.getRegisteredRole(idInput)
                val fullName = model.getFullName(idInput)
                val emailAddress = model.getEmailAddress(idInput)
                view.navigateToDashboard(registeredRole, fullName, idInput, emailAddress)
            } else {
                view.showPasswordError("Invalid Password")
            }
        }
    }
}
