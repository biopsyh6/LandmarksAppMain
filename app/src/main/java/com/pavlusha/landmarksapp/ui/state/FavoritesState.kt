package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.LandmarkDomainModel

data class FavoritesState(
    val isLoading: Boolean = true,
    val landmarks: List<LandmarkDomainModel> = emptyList(),
    val error: Int? = null
)
