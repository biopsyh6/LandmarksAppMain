package com.pavlusha.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.pavlusha.data.local.entity.ARAnnotationEntity
import com.pavlusha.data.local.entity.ARContentConfigEntity
import com.pavlusha.data.local.entity.HistoricalPeriodEntity

data class ARDataWithDetails(
    @Embedded val config: ARContentConfigEntity,

    @Relation(parentColumn = "landmarkId", entityColumn = "landmarkId")
    val annotations: List<ARAnnotationEntity>,

    @Relation(parentColumn = "selectedPeriodId", entityColumn = "periodId")
    val selectedPeriod: HistoricalPeriodEntity?
)
