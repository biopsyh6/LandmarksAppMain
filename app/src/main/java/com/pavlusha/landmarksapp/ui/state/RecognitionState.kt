package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.ARContentDomainModel
import com.pavlusha.domain.model.LandmarkDomainModel

data class RecognitionState(
    val mode: RecognitionMode = RecognitionMode.SCANNING,
    val recognizedLandmark: LandmarkDomainModel? = null,
    val arContent: ARContentDomainModel? = null,
    val isAnalyzing: Boolean = false,
    val distanceMeters: Float? = null,
    val navigationBearing: Float? = null
)

enum class RecognitionMode {
    SCANNING,
    FOUND_UI,
    AR_VIEW,
    SIMPLE_3D_VIEW
}