package com.pavlusha.landmarksapp.ui.intent

import com.pavlusha.domain.model.SearchResultDomainModel

sealed interface MapIntent {
    data object OnMyLocationClicked : MapIntent
    data object StopTracking : MapIntent
    data object StartTracking : MapIntent
    data object OnMapInitialized : MapIntent
    data class OnMapCameraMoved(
        val latitude: Double,
        val longitude: Double
    ) : MapIntent

    data class OnSearchQueryChanged(val query: String) : MapIntent
    data object OnSearchExecute : MapIntent
    data object OnSearchNearby : MapIntent
    data class OnLandmarkClicked(val landmark: SearchResultDomainModel) : MapIntent
    data object OnCloseLandmarkInfo : MapIntent
    data class OnLandmarkDetailsClicked(val landmark: SearchResultDomainModel) : MapIntent

}