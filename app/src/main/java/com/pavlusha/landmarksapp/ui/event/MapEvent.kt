package com.pavlusha.landmarksapp.ui.event

import com.pavlusha.domain.model.LandmarkSource

sealed interface MapEvent {
    data class ShowToast(val message: Int) : MapEvent
    data class NavigateToLandmarkDetails(val landmarkId: String, val source: LandmarkSource) : MapEvent
}