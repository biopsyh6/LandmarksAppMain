package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landmark_source_urls")
data class LandmarkSourceUrlEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val landmarkId: String,
    val url: String
)
