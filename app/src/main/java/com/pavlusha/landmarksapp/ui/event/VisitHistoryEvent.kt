package com.pavlusha.landmarksapp.ui.event

import com.pavlusha.domain.model.LandmarkSource

sealed interface VisitHistoryEvent {
    data class ShowToast(val message: String) : VisitHistoryEvent
    data class NavigateToDetails(val landmarkId: String, val source: LandmarkSource) : VisitHistoryEvent
    data object NavigateBack : VisitHistoryEvent
}