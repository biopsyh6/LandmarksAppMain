package com.pavlusha.landmarksapp.ui.intent

import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.UserNoteDomainModel

sealed interface MyNotesIntent {
    data object LoadNotes : MyNotesIntent
    data class DeleteNote(val noteId: String) : MyNotesIntent
    data class UpdateNote(val note: UserNoteDomainModel, val newText: String) : MyNotesIntent
    data class OnLandmarkClicked(val landmarkId: String, val source: LandmarkSource) : MyNotesIntent
    data object OnBackClicked : MyNotesIntent
}