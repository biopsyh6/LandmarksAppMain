package com.pavlusha.landmarksapp.ui.intent

import com.pavlusha.domain.model.LandmarkSource

sealed interface FavoritesIntent {
    data object LoadFavorites : FavoritesIntent
    data class OnRemoveFavoriteClicked(val landmarkId: String) : FavoritesIntent
    data class OnLandmarkClicked(val landmarkId: String, val source: LandmarkSource) : FavoritesIntent
    data object OnBackClicked : FavoritesIntent
}