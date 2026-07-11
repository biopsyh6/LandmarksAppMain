package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.VisitWithLandmarkDomainModel

data class VisitHistoryState(
    val isLoading: Boolean = true,
    val visits: List<VisitWithLandmarkDomainModel> = emptyList(),
    val error: Int? = null
)
