package com.pavlusha.landmarksapp.ui.intent

sealed interface ProfileIntent {
    data object LoadUser : ProfileIntent
    data object SignOut : ProfileIntent
    data class OnMenuItemClicked(val route: String) : ProfileIntent

    data object OnEditClicked : ProfileIntent
    data object OnCancelEditClicked : ProfileIntent
    data class OnNameChanged(val name: String) : ProfileIntent
    data class OnPhotoChanged(val photoUri: String) : ProfileIntent
    data object OnSaveProfileClicked : ProfileIntent
}