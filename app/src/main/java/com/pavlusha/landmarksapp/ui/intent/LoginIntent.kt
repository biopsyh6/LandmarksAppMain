package com.pavlusha.landmarksapp.ui.intent

sealed interface LoginIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent

    data object SignInClicked : LoginIntent
    data object GoToRegisterClicked : LoginIntent
    data object ForgotPasswordClicked : LoginIntent
}