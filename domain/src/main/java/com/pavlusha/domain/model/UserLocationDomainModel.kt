package com.pavlusha.domain.model

data class UserLocationDomainModel(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val bearing: Float? = null, // направление взгляда
    val timestamp: Long,
    val altitude: Double? = null,
)
