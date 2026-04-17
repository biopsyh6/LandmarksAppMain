package com.pavlusha.domain.model

data class LandmarkDomainModel(
    val id: String,
    val name: String,
    val description: String,
    val shortDescription: String,
    val latitude: Double,
    val longitude: Double,
    val accuracyRadius: Int = 50,
    val category: LandmarkCategoryDomainModel,

    val currentModel3dPath: String? = null,
    val historicalPeriods: List<HistoricalPeriodDomainModel> = emptyList(),

    val mainImageUrl: String? = null,
    val thumbnailUrl: String? = null,
    val galleryUrls: List<String> = emptyList(),

    val tags: List<String> = emptyList(),
    val sourceUrls: List<String> = emptyList(),

    val isFavorite: Boolean = false,
    val distanceMeters: Float? = null,
    val isAvailableOffline: Boolean = false
)
