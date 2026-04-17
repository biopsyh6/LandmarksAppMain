package com.pavlusha.domain.model

data class GeoFenceDomainModel(
    val landmarkId: String,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Float
)
