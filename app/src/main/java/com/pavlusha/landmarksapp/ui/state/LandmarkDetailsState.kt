package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.WikipediaInfoDomainModel

data class LandmarkDetailsState(
    val isLoading: Boolean = true,
    val error: Int? = null,
    val landmark: LandmarkDomainModel? = null,
    val selectedTab: DetailsTab = DetailsTab.STATIC_INFO,
    val isWikiLoading: Boolean = false,
    val wikipediaInfo: WikipediaInfoDomainModel? = null
)

enum class DetailsTab {
    STATIC_INFO,
    AR_MODE
}