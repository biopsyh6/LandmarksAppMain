package com.pavlusha.data.model.remote

data class RegionPackageRemoteModel(
    val regionId: String = "",
    val regionName: String = "",
    val sizeMb: Float = 0f,
    val downloadUrl: String = "",
    val lastUpdated: Long = 0L
)
