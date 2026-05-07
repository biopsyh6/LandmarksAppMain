package com.pavlusha.data.model.remote

data class UserRemoteModel(
    val id: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val isAnonymous: Boolean = false
)
