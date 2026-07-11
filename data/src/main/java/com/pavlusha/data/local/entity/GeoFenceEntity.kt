package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "geo_fences",
    foreignKeys = [
        ForeignKey(
            entity = LandmarkEntity::class,
            parentColumns = ["id"],
            childColumns = ["landmarkId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("landmarkId")]
)
data class GeoFenceEntity(
    @PrimaryKey val landmarkId: String,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Float
)
