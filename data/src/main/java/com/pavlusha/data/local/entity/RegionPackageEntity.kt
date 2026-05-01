package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_regions")
data class RegionPackageEntity(
    @PrimaryKey val regionId: String,
    val regionName: String,
    val sizeMb: Float,
    val status: String,
    val progress: Float,
    val lastUpdated: Long?
)