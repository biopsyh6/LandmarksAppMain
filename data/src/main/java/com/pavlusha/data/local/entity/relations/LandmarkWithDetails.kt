package com.pavlusha.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.pavlusha.data.local.entity.ExternalInfoEntity
import com.pavlusha.data.local.entity.HistoricalPeriodEntity
import com.pavlusha.data.local.entity.LandmarkEntity
import com.pavlusha.data.local.entity.LandmarkGalleryEntity
import com.pavlusha.data.local.entity.LandmarkSourceUrlEntity
import com.pavlusha.data.local.entity.LandmarkTagEntity

data class LandmarkWithDetails(
    @Embedded val landmark: LandmarkEntity,

    @Relation(parentColumn = "id", entityColumn = "landmarkId")
    val periods: List<HistoricalPeriodEntity>,

    @Relation(parentColumn = "id", entityColumn = "landmarkId")
    val gallery: List<LandmarkGalleryEntity>,

    @Relation(parentColumn = "id", entityColumn = "landmarkId")
    val tags: List<LandmarkTagEntity>,

    @Relation(parentColumn = "id", entityColumn = "landmarkId")
    val sourceUrls: List<LandmarkSourceUrlEntity>,

    @Relation(parentColumn = "id", entityColumn = "landmarkId")
    val externalInfo: List<ExternalInfoEntity>
)
