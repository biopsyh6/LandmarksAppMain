package com.pavlusha.data.model.remote

data class GeoFenceRemoteModel(
    val landmarkId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val radiusMeters: Float = 0f
)
