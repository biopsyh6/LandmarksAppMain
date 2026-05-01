package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landmark_gallery")
data class LandmarkGalleryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val landmarkId: String,
    val imageUrl: String
)
