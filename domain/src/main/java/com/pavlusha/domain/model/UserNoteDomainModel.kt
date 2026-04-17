package com.pavlusha.domain.model

data class UserNoteDomainModel(
    val id: String,
    val landmarkId: String,
    val text: String,
    val createdAt: Long,
    val updatedAt: Long
)
