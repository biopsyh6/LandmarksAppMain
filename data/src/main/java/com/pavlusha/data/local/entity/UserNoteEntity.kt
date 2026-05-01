package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_notes",
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
data class UserNoteEntity(
    @PrimaryKey val id: String,
    val landmarkId: String,
    val text: String,
    val createdAt: Long,
    val updatedAt: Long
)
