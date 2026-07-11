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
    val remoteModel3dPath: String? = null,
    val localModel3dPath: String? = null,

    val historicalPeriods: List<HistoricalPeriodDomainModel> = emptyList(),

    val mainImageUrl: String? = null,
    val remoteMainImageUrl: String? = null,
    val localMainImagePath: String? = null,

    val thumbnailUrl: String? = null,
    val galleryUrls: List<String> = emptyList(),

    val tags: List<String> = emptyList(),
    val sourceUrls: List<String> = emptyList(),

    val isFavorite: Boolean = false,
    val distanceMeters: Float? = null,
    val isAvailableOffline: Boolean = false,

    val isPromoted: Boolean = false,
    val source: LandmarkSource = LandmarkSource.LOCAL_DB,
    val externalInfo: List<ExternalInfoDomainModel> = emptyList()
)

enum class LandmarkSource {
    LOCAL_DB,
    REMOTE_API,
    USER_ADDED
}

data class ExternalInfoDomainModel(
    val sourceName: String,
    val webUrl: String,
    val summary: String?,
    val rating: Float? = null,
)
