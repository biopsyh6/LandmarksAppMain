package com.pavlusha.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "historical_periods",
    foreignKeys = [ForeignKey(
        entity = LandmarkEntity::class,
        parentColumns = ["id"],
        childColumns = ["landmarkId"],
        onDelete = ForeignKey.CASCADE

    )]
)
data class HistoricalPeriodEntity(
    @PrimaryKey val periodId: String,
    val landmarkId: String,
    val name: String,
    val yearFrom: Int?,
    val yearTo: Int?,
    val description: String?,
    val model3dPath: String?
)
