package com.pavlusha.data.model

data class UserLocationDataModel(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float?,
    val altitude: Double?,
    val speed: Float?,
    val bearing: Float?,
    val timestamp: Long
)
