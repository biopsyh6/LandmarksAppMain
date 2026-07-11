package com.pavlusha.data.mapper

import com.pavlusha.data.local.entity.ARAnnotationEntity
import com.pavlusha.data.local.entity.ARContentConfigEntity
import com.pavlusha.data.local.entity.HistoricalPeriodEntity
import com.pavlusha.data.local.entity.relations.ARDataWithDetails
import com.pavlusha.domain.model.ARAnnotation
import com.pavlusha.domain.model.ARContentDomainModel
import com.pavlusha.domain.model.ARDisplayMode
import com.pavlusha.domain.model.ARPlacementType
import com.pavlusha.domain.model.HistoricalPeriodDomainModel

object ARContentLocalDataMapper {
    fun toDomainFromData(data: ARDataWithDetails): ARContentDomainModel {
        return ARContentDomainModel(
            landmarkId = data.config.landmarkId,
            displayMode = ARDisplayMode.valueOf(data.config.displayMode),
            placementType = ARPlacementType.valueOf(data.config.placementType),
            activeModel3dPath = data.config.activeModel3dPath,
            selectedPeriod = data.selectedPeriod?.toDomain(),
            textAnnotations = data.annotations.map { it.toDomain() },
            modelScale = data.config.modelScale,
            heightOffset = data.config.heightOffset,
            rotationDegrees = data.config.rotationDegrees,
            showDistance = data.config.showDistance,
            showCategory = data.config.showCategory,
            showPeriodName = data.config.showPeriodName,
            isModelLoaded = false
        )
    }

    fun fromDomainToData(domain: ARContentDomainModel): ARDataWithDetails {
        return ARDataWithDetails(
            config = fromDomainToConfigEntity(domain),
            annotations = domain.textAnnotations.map { it.fromDomain(domain.landmarkId) },
            selectedPeriod = domain.selectedPeriod?.let { fromDomainToPeriodEntity(it, domain.landmarkId) }
        )
    }

    fun fromDomainToConfigEntity(domain: ARContentDomainModel): ARContentConfigEntity {
        return ARContentConfigEntity(
            landmarkId = domain.landmarkId,
            selectedPeriodId = domain.selectedPeriod?.id,
            activeModel3dPath = domain.activeModel3dPath,
            displayMode = domain.displayMode.name,
            placementType = domain.placementType.name,
            modelScale = domain.modelScale,
            heightOffset = domain.heightOffset,
            rotationDegrees = domain.rotationDegrees,
            showDistance = domain.showDistance,
            showCategory = domain.showCategory,
            showPeriodName = domain.showPeriodName
        )
    }

    fun toDomainFromConfig(entity: ARContentConfigEntity): ARContentDomainModel {
        return ARContentDomainModel(
            landmarkId = entity.landmarkId,
            displayMode = ARDisplayMode.valueOf(entity.displayMode),
            placementType = ARPlacementType.valueOf(entity.placementType),
            activeModel3dPath = entity.activeModel3dPath,
            selectedPeriod = null,
            textAnnotations = emptyList(),
            modelScale = entity.modelScale,
            heightOffset = entity.heightOffset,
            rotationDegrees = entity.rotationDegrees,
            showDistance = entity.showDistance,
            showCategory = entity.showCategory,
            showPeriodName = entity.showPeriodName,
            isModelLoaded = false
        )
    }



    fun ARAnnotationEntity.toDomain() = ARAnnotation(
        text = text,
        positionX = positionX,
        positionY = positionY,
        positionZ = positionZ,
        colorHex = colorHex
    )

    fun ARAnnotation.fromDomain(landmarkId: String) = ARAnnotationEntity(
        landmarkId = landmarkId,
        text = text,
        positionX = positionX,
        positionY = positionY,
        positionZ = positionZ,
        colorHex = colorHex
    )

    private fun HistoricalPeriodEntity.toDomain() = HistoricalPeriodDomainModel(
        id = periodId,
        name = name,
        yearFrom = yearFrom,
        yearTo = yearTo,
        description = description,
        model3dPath = model3dPath
    )

    private fun fromDomainToPeriodEntity(domain: HistoricalPeriodDomainModel, landmarkId: String) =
        HistoricalPeriodEntity(
            periodId = domain.id,
            landmarkId = landmarkId,
            name = domain.name,
            yearFrom = domain.yearFrom,
            yearTo = domain.yearTo,
            description = domain.description,
            model3dPath = domain.model3dPath
        )
}