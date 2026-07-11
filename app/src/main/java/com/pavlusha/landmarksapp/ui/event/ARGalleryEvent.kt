package com.pavlusha.landmarksapp.ui.event

sealed interface ARGalleryEvent {
    data class ShowToast(val message: String) : ARGalleryEvent
    data object NavigateBack : ARGalleryEvent
}