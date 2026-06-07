package com.pavlusha.landmarksapp.ui.event

import com.pavlusha.domain.model.LandmarkSource

sealed interface FavoritesEvent {
    data class ShowToast(val message: Int) : FavoritesEvent
    data class NavigateToDetails(val landmarkId: String, val source: LandmarkSource) : FavoritesEvent
    data object NavigateBack : FavoritesEvent
}