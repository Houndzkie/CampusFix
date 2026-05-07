package com.example.campusfix.screens.login

interface LoginContract {
    interface View {
        fun showSchoolIDError(message: String)
        fun showPasswordError(message: String)
        fun navigateToDashboard(role: String?, name: String?, id: String?, email: String?)
    }

    interface Presenter {
        fun login(idInput: String, passwordInput: String)
    }
}
