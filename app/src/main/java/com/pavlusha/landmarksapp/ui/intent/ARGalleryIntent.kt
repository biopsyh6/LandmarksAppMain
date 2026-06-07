package com.pavlusha.landmarksapp.ui.intent

import android.content.Context
import com.pavlusha.domain.model.UserARPhotoDomainModel

sealed interface ARGalleryIntent {
    data object LoadPhotos : ARGalleryIntent
    data class OnPhotoClicked(val photo: UserARPhotoDomainModel) : ARGalleryIntent
    data object OnCloseFullscreen : ARGalleryIntent
    data class DeletePhoto(val photoId: String) : ARGalleryIntent
    data class SaveToDeviceGallery(val context: Context, val photo: UserARPhotoDomainModel) : ARGalleryIntent
    data object OnBackClicked : ARGalleryIntent
}