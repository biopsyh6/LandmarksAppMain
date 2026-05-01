package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.LandmarkDomainModel

data class LandmarkDetailsState(
    val isLoading: Boolean = true,
    val error: Int? = null,
    val landmark: LandmarkDomainModel? = null,
    val selectedTab: DetailsTab = DetailsTab.STATIC_INFO
)

enum class DetailsTab {
    STATIC_INFO,
    AR_MODE
}