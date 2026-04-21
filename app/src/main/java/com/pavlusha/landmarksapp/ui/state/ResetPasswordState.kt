package com.pavlusha.landmarksapp.ui.state

data class ResetPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: Int? = null,
    val isSuccess: Boolean = false
)
