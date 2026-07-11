package com.pavlusha.landmarksapp.ui.event

sealed interface LandmarkDetailsEvent {
    data object NavigateBack : LandmarkDetailsEvent
    data class ShowToast(val message: Int) : LandmarkDetailsEvent
}