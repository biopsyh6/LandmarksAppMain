package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ar_content_configs",
    foreignKeys = [
        ForeignKey(
            entity = LandmarkEntity::class,
            parentColumns = ["id"],
            childColumns = ["landmarkId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HistoricalPeriodEntity::class,
            parentColumns = ["periodId"],
            childColumns = ["selectedPeriodId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("landmarkId"), Index("selectedPeriodId")]
)
data class ARContentConfigEntity(
    @PrimaryKey val landmarkId: String,

    val selectedPeriodId: String?,

    val activeModel3dPath: String?,
    val displayMode: String,
    val placementType: String,
    val modelScale: Float,
    val heightOffset: Float,
    val rotationDegrees: Float,
    val showDistance: Boolean,
    val showCategory: Boolean,
    val showPeriodName: Boolean
)
