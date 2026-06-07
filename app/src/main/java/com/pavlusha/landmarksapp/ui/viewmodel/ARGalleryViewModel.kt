package com.pavlusha.landmarksapp.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserARPhotoDomainModel
import com.pavlusha.domain.usecase.DeleteARPhotoUseCase
import com.pavlusha.domain.usecase.GetUserGalleryUseCase
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.ARGalleryEvent
import com.pavlusha.landmarksapp.ui.intent.ARGalleryIntent
import com.pavlusha.landmarksapp.ui.state.ARGalleryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class ARGalleryViewModel(
    private val getUserGalleryUseCase: GetUserGalleryUseCase,
    private val deleteARPhotoUseCase: DeleteARPhotoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ARGalleryState())
    val state: StateFlow<ARGalleryState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<ARGalleryEvent>(viewModelScope)
    val event = _event.flow

    init {
        onIntent(ARGalleryIntent.LoadPhotos)
    }

    fun onIntent(intent: ARGalleryIntent) {
        when (intent) {
            is ARGalleryIntent.LoadPhotos -> loadPhotos()
            is ARGalleryIntent.OnPhotoClicked -> _state.update { it.copy(selectedPhoto = intent.photo) }
            is ARGalleryIntent.OnCloseFullscreen -> _state.update { it.copy(selectedPhoto = null) }
            is ARGalleryIntent.DeletePhoto -> deletePhoto(intent.photoId)
            is ARGalleryIntent.SaveToDeviceGallery -> exportToMediaStore(intent.context, intent.photo)
            is ARGalleryIntent.OnBackClicked -> navigateBack()
        }
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = getUserGalleryUseCase()

            when (result) {
                is TResult.Success -> {
                    _state.update { it.copy(isLoading = false, photos = result.data) }
                }
                is TResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(ARGalleryEvent.ShowToast("Ошибка загрузки галереи"))
                }
            }
        }
    }

    private fun deletePhoto(photoId: String) {
        viewModelScope.launch {
            val result = deleteARPhotoUseCase(photoId)
            if (result is TResult.Success) {
                _state.update { it.copy(selectedPhoto = null) }
                loadPhotos()
                _event.emit(ARGalleryEvent.ShowToast("Фото удалено"))
            } else {
                _event.emit(ARGalleryEvent.ShowToast("Ошибка при удалении"))
            }
        }
    }

    private fun exportToMediaStore(context: Context, photo: UserARPhotoDomainModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = File(photo.localFilePath)
                if (!file.exists()) {
                    _event.emit(ARGalleryEvent.ShowToast("Файл не найден"))
                    return@launch
                }

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "LandmarkAR_${System.currentTimeMillis()}.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/LandmarksAR")
                }

                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    context.contentResolver.openOutputStream(uri)?.use { outStream ->
                        FileInputStream(file).use { inStream ->
                            inStream.copyTo(outStream)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        _event.emit(ARGalleryEvent.ShowToast("Сохранено в галерею устройства!"))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _event.emit(ARGalleryEvent.ShowToast("Ошибка сохранения: ${e.message}"))
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _event.emit(ARGalleryEvent.NavigateBack)
        }
    }
}