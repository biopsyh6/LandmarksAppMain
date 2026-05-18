package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_ar_photos",
    foreignKeys = [
        ForeignKey(
            entity = LandmarkEntity::class,
            parentColumns = ["id"],
            childColumns = ["landmarkId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("landmarkId"), Index("userId")]
)
data class UserARPhotoEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val landmarkId: String,
    val localFilePath: String,
    val remoteUrl: String?,
    val createdAt: Long
)
