package com.pavlusha.data.model.remote

data class ARContentRemoteModel(
    val landmarkId: String = "",
    val displayMode: String = "CURRENT",
    val placementType: String = "CENTER_SCREEN_HIT",
    val activeModel3dPath: String? = null,

    val selectedPeriodId: String? = null,

    val textAnnotations: List<ARAnnotationRemoteModel> = emptyList(),

    val modelScale: Float = 1.0f,
    val heightOffset: Float = 0f,
    val rotationDegrees: Float = 0f,

    val showDistance: Boolean = true,
    val showCategory: Boolean = true,
    val showPeriodName: Boolean = true
)

data class ARAnnotationRemoteModel(
    val text: String = "",
    val positionX: Float = 0f,
    val positionY: Float = 0f,
    val positionZ: Float = 2.5f,
    val colorHex: String = "#FFFFFF"
)
