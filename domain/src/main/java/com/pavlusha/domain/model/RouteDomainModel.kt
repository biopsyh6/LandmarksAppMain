package com.pavlusha.domain.model

data class RouteDomainModel(
    val points: List<GeoCoordinateDomainModel>
)

data class GeoCoordinateDomainModel(
    val latitude: Double,
    val longitude: Double
)
