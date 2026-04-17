package com.pavlusha.domain.model

data class UserLocationDomainModel(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long
)
