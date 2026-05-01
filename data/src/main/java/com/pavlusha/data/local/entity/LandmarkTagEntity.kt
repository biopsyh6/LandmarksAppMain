package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landmark_tags")
data class LandmarkTagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val landmarkId: String,
    val tag: String
)
