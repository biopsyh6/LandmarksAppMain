package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.UserARPhotoDomainModel

data class ARGalleryState(
    val isLoading: Boolean = true,
    val photos: List<UserARPhotoDomainModel> = emptyList(),
    val selectedPhoto: UserARPhotoDomainModel? = null,
    val error: Int? = null
)
