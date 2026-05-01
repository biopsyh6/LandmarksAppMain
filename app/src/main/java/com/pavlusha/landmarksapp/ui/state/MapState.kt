package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.domain.model.UserLocationDomainModel

data class MapState(
    val userLocation: UserLocationDomainModel? = null,
    val isMapInitialized: Boolean = false,
    val isLoading: Boolean = true,
    val error: Int? = null,
    val isTrackingActive: Boolean = true,

    val isFirstLocationFix: Boolean = true,

    val searchQuery: String = "",
    val mapLandmarks: List<SearchResultDomainModel> = emptyList(),
    val selectedLandmark: SearchResultDomainModel? = null
)
