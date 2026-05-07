package com.pavlusha.data.model.remote

data class LandmarkRemoteModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val shortDescription: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracyRadius: Int = 50,

    val category: LandmarkCategoryRemoteModel = LandmarkCategoryRemoteModel(),

    val remoteModel3dPath: String? = null,
    val remoteMainImageUrl: String? = null,
    val thumbnailUrl: String? = null,

    val galleryUrls: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val sourceUrls: List<String> = emptyList(),

    val historicalPeriods: List<HistoricalPeriodRemoteModel> = emptyList(),
    val externalInfo: List<ExternalInfoRemoteModel> = emptyList(),

    val isPromoted: Boolean = false,
    val source: String = "REMOTE_API"
)

data class LandmarkCategoryRemoteModel(
    val id: String = "",
    val name: String = "",
    val iconName: String = "",
    val colorHex: String = "",
    val type: String = "OTHER",
    val rawTypeName: String = ""
)

data class HistoricalPeriodRemoteModel(
    val id: String = "",
    val name: String = "",
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val description: String? = null,
    val model3dPath: String? = null
)

data class ExternalInfoRemoteModel(
    val sourceName: String = "",
    val webUrl: String = "",
    val summary: String? = null,
    val rating: Float? = null
)
