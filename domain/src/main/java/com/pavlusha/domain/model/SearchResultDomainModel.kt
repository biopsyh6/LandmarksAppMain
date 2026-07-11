package com.pavlusha.domain.model

data class SearchResultDomainModel(
    val id: String,
    val name: String,
    val address: String?,
    val descriptionFromCategories: String?,
    val latitude: Double,
    val longitude: Double,
    val categoryName: String,
    val thumbnailUrl: String?,
    val distanceMeters: Float?,
    val source: LandmarkSource,
    val isPromoted: Boolean = false
)
