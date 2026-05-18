package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "landmark_source_urls",
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
data class LandmarkSourceUrlEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val landmarkId: String,
    val url: String
)
