package com.pavlusha.landmarksapp.ui.state

import com.pavlusha.domain.model.NoteWithLandmarkDomainModel

data class MyNotesState(
    val isLoading: Boolean = true,
    val notes: List<NoteWithLandmarkDomainModel> = emptyList(),
    val error: Int? = null
)
