package com.pavlusha.landmarksapp.ui.state

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: Int? = null
)
