package com.pavlusha.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landmarks")
data class LandmarkEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val shortDescription: String,
    val latitude: Double,
    val longitude: Double,
    val accuracyRadius: Int,

    @Embedded(prefix = "category_") val category: LandmarkCategoryEntity,

    val currentModel3dPath: String?,
    val remoteModel3dPath: String?,
    val localModel3dPath: String?,
    val mainImageUrl: String?,
    val remoteMainImageUrl: String?,
    val localMainImagePath: String?,
    val thumbnailUrl: String?,

    val isFavorite: Boolean,
    val isAvailableOffline: Boolean,
    val isPromoted: Boolean,
    val source: String
)

data class LandmarkCategoryEntity(
    val categoryId: String,
    val categoryName: String,
    val iconName: String,
    val colorHex: String,
    val type: String,
    val rawTypeName: String
)
