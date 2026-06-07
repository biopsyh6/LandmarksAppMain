package com.pavlusha.landmarksapp.ui.intent

import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.landmarksapp.ui.state.DetailsTab

sealed interface LandmarkDetailsIntent {
    data class LoadLandmark(val id: String, val source: LandmarkSource) : LandmarkDetailsIntent
    data class OnTabSelected(val tab: DetailsTab) : LandmarkDetailsIntent
    data object OnBackClicked : LandmarkDetailsIntent
    data object OnToggleFavorite : LandmarkDetailsIntent
    data class OnSaveNoteClicked(val text: String) : LandmarkDetailsIntent
}