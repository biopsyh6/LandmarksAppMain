package com.pavlusha.landmarksapp.ui.event

sealed interface AuthEvent {
    data class ShowToast(val message: Int) : AuthEvent
    data object NavigateToHome : AuthEvent

    data object NavigateToRegister : AuthEvent
    data object NavigateToLogin : AuthEvent
    data object NavigateToResetPassword : AuthEvent
}