package com.pavlusha.landmarksapp.ui.event

sealed interface ProfileEvent {
    data class ShowToast(val message: Int) : ProfileEvent
    data object NavigateToAuth : ProfileEvent
    data class NavigateTo(val route: String) : ProfileEvent
}