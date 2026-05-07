package com.pavlusha.data.mapper.remote

import com.pavlusha.data.model.remote.ARAnnotationRemoteModel
import com.pavlusha.data.model.remote.ARContentRemoteModel
import com.pavlusha.domain.model.ARAnnotation
import com.pavlusha.domain.model.ARContentDomainModel
import com.pavlusha.domain.model.ARDisplayMode
import com.pavlusha.domain.model.ARPlacementType

object ARContentRemoteDataMapper {
    fun toDomainFromData(remote: ARContentRemoteModel): ARContentDomainModel {
        return ARContentDomainModel(
            landmarkId = remote.landmarkId,
            displayMode = runCatching { ARDisplayMode.valueOf(remote.displayMode) }.getOrDefault(ARDisplayMode.CURRENT),
            placementType = runCatching { ARPlacementType.valueOf(remote.placementType) }.getOrDefault(ARPlacementType.CENTER_SCREEN_HIT),
            activeModel3dPath = remote.activeModel3dPath,

            selectedPeriod = null,

            textAnnotations = remote.textAnnotations.map { it.toDomain() },
            modelScale = remote.modelScale,
            heightOffset = remote.heightOffset,
            rotationDegrees = remote.rotationDegrees,
            showDistance = remote.showDistance,
            showCategory = remote.showCategory,
            showPeriodName = remote.showPeriodName,
            isModelLoaded = false
        )
    }

    fun fromDomainToData(domain: ARContentDomainModel): ARContentRemoteModel {
        return ARContentRemoteModel(
            landmarkId = domain.landmarkId,
            displayMode = domain.displayMode.name,
            placementType = domain.placementType.name,
            activeModel3dPath = domain.activeModel3dPath,
            selectedPeriodId = domain.selectedPeriod?.id,
            textAnnotations = domain.textAnnotations.map { it.fromDomain() },
            modelScale = domain.modelScale,
            heightOffset = domain.heightOffset,
            rotationDegrees = domain.rotationDegrees,
            showDistance = domain.showDistance,
            showCategory = domain.showCategory,
            showPeriodName = domain.showPeriodName
        )
    }

    private fun ARAnnotationRemoteModel.toDomain() = ARAnnotation(
        text = text,
        positionX = positionX,
        positionY = positionY,
        positionZ = positionZ,
        colorHex = colorHex
    )

    private fun ARAnnotation.fromDomain() = ARAnnotationRemoteModel(
        text = text,
        positionX = positionX,
        positionY = positionY,
        positionZ = positionZ,
        colorHex = colorHex
    )
}