package com.pavlusha.landmarksapp.ui.state

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: Int? = null
)
