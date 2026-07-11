package com.pavlusha.domain.model

data class UserDomainModel(
    val id: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val isAnonymous: Boolean
)
