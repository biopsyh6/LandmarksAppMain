package com.pavlusha.landmarksapp.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.usecase.GetARContentForLandmarkUseCase
import com.pavlusha.domain.usecase.IdentifyLandmarkUseCase
import com.pavlusha.domain.usecase.SaveARPhotoUseCase
import com.pavlusha.landmarksapp.ui.state.RecognitionMode
import com.pavlusha.landmarksapp.ui.state.RecognitionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class RecognitionViewModel(
    private val identifyLandmarkUseCase: IdentifyLandmarkUseCase,
    private val getARContentForLandmarkUseCase: GetARContentForLandmarkUseCase,
    private val saveARPhotoUseCase: SaveARPhotoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RecognitionState())
    val state: StateFlow<RecognitionState> = _state.asStateFlow()

    private var recognitionCount = 0
    private var lastRecognizedClass: String? = null
    private val requiredFramesToConfirm = 10

    fun setMode(mode: RecognitionMode) {
        _state.update { it.copy(mode = mode) }
        if (mode == RecognitionMode.SCANNING) {
            _state.update { it.copy(recognizedLandmark = null) }
            recognitionCount = 0
            lastRecognizedClass = null
        }
    }

    fun analyzeFrame(imageProxy: ImageProxy) {
        if (state.value.mode != RecognitionMode.SCANNING || state.value.isAnalyzing) {
            imageProxy.close()
            return
        }

        _state.update { it.copy(isAnalyzing = true) }

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val bitmap = imageProxy.toBitmap()
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val imageBytes = stream.toByteArray()
                val rotation = imageProxy.imageInfo.rotationDegrees

                val result = identifyLandmarkUseCase(imageBytes, rotation)

                when (result) {
                    is TResult.Success -> {
                        val landmark = result.data
                        if (lastRecognizedClass == landmark.name) {
                            recognitionCount++
                        } else {
                            lastRecognizedClass = landmark.name
                            recognitionCount = 1
                        }

                        if (recognitionCount >= requiredFramesToConfirm) {
                            _state.update {
                                it.copy(
                                    mode = RecognitionMode.FOUND_UI,
                                    recognizedLandmark = landmark
                                )
                            }
                            loadARContent(landmark.id)
                        }
                    }
                    is TResult.Error -> {
                        Log.e("ML_RECOGNITION", "Analysis error: ${result.exception?.message}")
                        recognitionCount = 0
                        lastRecognizedClass = null
                    }
                }
            } catch (e: Exception) {
                Log.e("ML_RECOGNITION", "Fatal analysis error", e)
            } finally {
                _state.update { it.copy(isAnalyzing = false) }
                imageProxy.close()
            }
        }
    }

    private fun loadARContent(landmarkId: String) {
        viewModelScope.launch {
            val result = getARContentForLandmarkUseCase(landmarkId)
            if (result is TResult.Success) {
                _state.update { it.copy(arContent = result.data) }
            }
        }
    }

    fun saveARPhoto(landmarkId: String, bitmap: Bitmap, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                val imageBytes = stream.toByteArray()

                val result = saveARPhotoUseCase(landmarkId, imageBytes)

                withContext(Dispatchers.Main) {
                    onResult(result is TResult.Success)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }
}