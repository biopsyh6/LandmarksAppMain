package com.pavlusha.landmarksapp.ui.intent

import com.pavlusha.domain.model.LandmarkSource

sealed interface VisitHistoryIntent {
    data object LoadHistory : VisitHistoryIntent
    data class OnLandmarkClicked(val landmarkId: String, val source: LandmarkSource) : VisitHistoryIntent
    data object OnBackClicked : VisitHistoryIntent
}