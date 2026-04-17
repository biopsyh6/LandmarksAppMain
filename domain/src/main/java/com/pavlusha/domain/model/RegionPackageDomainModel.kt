package com.pavlusha.domain.model

data class RegionPackageDomainModel(
    val regionId: String,
    val regionName: String,
    val sizeMb: Float,
    val isDownloaded: Boolean,
    val lastUpdated: Long?
)
