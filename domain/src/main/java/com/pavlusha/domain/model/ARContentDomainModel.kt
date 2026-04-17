package com.pavlusha.domain.model

data class ARContentDomainModel(
    val landmarkId: String,
    val displayMode: ARDisplayMode = ARDisplayMode.CURRENT,
    val placementType: ARPlacementType = ARPlacementType.CENTER_SCREEN_HIT,
    val activeModel3dPath: String?,
    val selectedPeriod: HistoricalPeriodDomainModel? = null,
    val textAnnotations: List<ARAnnotation> = emptyList(),

    val modelScale: Float = 1.0f,
    val heightOffset: Float = 0f,
    val rotationDegrees: Float = 0f,

    val showDistance: Boolean = true,
    val showCategory: Boolean = true,
    val showPeriodName: Boolean = true,
)

enum class ARDisplayMode {
    CURRENT,
    HISTORICAL,
}

enum class ARPlacementType {
    CENTER_SCREEN_HIT,
    GPS_LOCATION,
    SCREEN_OVERLAY // Показывать поверх камеры (HUD)
}

data class ARAnnotation(
    val text: String,
    val positionX: Float = 0f,
    val positionY: Float = 0f,
    val positionZ: Float = 2.5f,
    val colorHex: String
)
