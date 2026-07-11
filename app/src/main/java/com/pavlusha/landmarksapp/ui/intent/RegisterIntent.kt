package com.pavlusha.landmarksapp.ui.intent

sealed interface RegisterIntent {
    data class EmailChanged(val value: String) : RegisterIntent
    data class PasswordChanged(val value: String) : RegisterIntent
    data class ConfirmPasswordChanged(val value: String) : RegisterIntent
    data object SignUpClicked : RegisterIntent
    data object GoToLoginClicked : RegisterIntent
}