package com.pavlusha.landmarksapp.ui.event

import com.pavlusha.domain.model.LandmarkSource

sealed interface MyNotesEvent {
    data class ShowToast(val message: String) : MyNotesEvent
    data class NavigateToDetails(val landmarkId: String, val source: LandmarkSource) : MyNotesEvent
    data object NavigateBack : MyNotesEvent
}