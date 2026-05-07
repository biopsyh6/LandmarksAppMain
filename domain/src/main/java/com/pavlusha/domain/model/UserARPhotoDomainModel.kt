package com.pavlusha.domain.model

data class UserARPhotoDomainModel(
    val id: String,
    val landmarkId: String,
    val localFilePath: String,
    val remoteUrl: String? = null,
    val createdAt: Long
)
