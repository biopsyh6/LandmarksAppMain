package com.pavlusha.landmarksapp.ui.intent

sealed interface ResetPasswordIntent {
    data class EmailChanged(val value: String) : ResetPasswordIntent
    data object ResetPasswordClicked : ResetPasswordIntent
    data object BackToLoginClicked : ResetPasswordIntent
}