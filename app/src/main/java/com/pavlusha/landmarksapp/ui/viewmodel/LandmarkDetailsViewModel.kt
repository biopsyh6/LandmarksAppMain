package com.pavlusha.landmarksapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.usecase.GetLandmarkDetailsUseCase
import com.pavlusha.domain.usecase.GetWikipediaInfoUseCase
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.LandmarkDetailsEvent
import com.pavlusha.landmarksapp.ui.intent.LandmarkDetailsIntent
import com.pavlusha.landmarksapp.ui.state.LandmarkDetailsState
import com.pavlusha.landmarksapp.util.parseToResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LandmarkDetailsViewModel(
    private val getLandmarkDetailsUseCase: GetLandmarkDetailsUseCase,
    private val getWikipediaInfoUseCase: GetWikipediaInfoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LandmarkDetailsState())
    val state: StateFlow<LandmarkDetailsState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<LandmarkDetailsEvent>(viewModelScope)
    val event = _event.flow

    fun onIntent(intent: LandmarkDetailsIntent) {
        when (intent) {
            is LandmarkDetailsIntent.LoadLandmark -> loadLandmark(intent.id, intent.source)
            is LandmarkDetailsIntent.OnTabSelected -> _state.update { it.copy(selectedTab = intent.tab) }
            is LandmarkDetailsIntent.OnBackClicked -> navigateBack()
            is LandmarkDetailsIntent.OnToggleFavorite -> toggleFavorite()
        }
    }

    private fun loadLandmark(id: String, source: LandmarkSource) {
        if (_state.value.landmark?.id == id && !_state.value.isLoading) return

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = getLandmarkDetailsUseCase(id, source)

            when (result) {
                is TResult.Success -> {
                    _state.update {
                        it.copy(isLoading = false, landmark = result.data)
                    }
                    loadWikipediaInfo(result.data.name)
                }
                is TResult.Error -> {
                    val errorRes = result.exception?.parseToResource()
                        ?: R.string.error_unknown

                    _state.update { it.copy(isLoading = false, error = errorRes) }
                    _event.emit(LandmarkDetailsEvent.ShowToast(errorRes))
                }
            }
        }
    }

    private fun loadWikipediaInfo(landmarkName: String) {
        _state.update { it.copy(isWikiLoading = true) }
        viewModelScope.launch {
            val result = getWikipediaInfoUseCase(landmarkName)
            when (result) {
                is TResult.Success -> {
                    _state.update { it.copy(isWikiLoading = false, wikipediaInfo = result.data) }
                }
                is TResult.Error -> {
                    _state.update { it.copy(isWikiLoading = false) }
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _event.emit(LandmarkDetailsEvent.NavigateBack)
        }
    }

    private fun toggleFavorite() {
        // TODO: Использовать ToggleFavoriteUseCase
    }
}