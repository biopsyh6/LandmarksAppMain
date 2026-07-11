package com.pavlusha.domain.model

data class RegionPackageDomainModel(
    val regionId: String,
    val regionName: String,
    val sizeMb: Float,
    val status: RegionStatus,
    val progress: Float = 0f,
    val lastUpdated: Long?
)

enum class RegionStatus {
    AVAILABLE, DOWNLOADING, PAUSED, COMPLETED, UPDATE_AVAILABLE
}