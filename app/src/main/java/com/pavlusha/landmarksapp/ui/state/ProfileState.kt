package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.UserDomainModel

data class ProfileState(
    val isLoading: Boolean = true,
    val user: UserDomainModel? = null,
    val error: Int? = null,

    val isEditing: Boolean = false,
    val editName: String = "",
    val editPhotoUrl: String? = null,
    val isSaving: Boolean = false
)
