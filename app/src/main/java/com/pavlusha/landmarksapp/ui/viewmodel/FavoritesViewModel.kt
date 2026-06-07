package com.pavlusha.landmarksapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.usecase.GetFavoriteLandmarksUseCase
import com.pavlusha.domain.usecase.ToggleFavoriteUseCase
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.FavoritesEvent
import com.pavlusha.landmarksapp.ui.intent.FavoritesIntent
import com.pavlusha.landmarksapp.ui.state.FavoritesState
import com.pavlusha.landmarksapp.util.parseToResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteLandmarksUseCase: GetFavoriteLandmarksUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<FavoritesEvent>(viewModelScope)
    val event = _event.flow

    init {
        onIntent(FavoritesIntent.LoadFavorites)
    }

    fun onIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.LoadFavorites -> loadFavorites()
            is FavoritesIntent.OnRemoveFavoriteClicked -> removeFavourite(intent.landmarkId)
            is FavoritesIntent.OnLandmarkClicked -> navigateToDetails(intent.landmarkId, intent.source)
            is FavoritesIntent.OnBackClicked -> navigateBack()
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = getFavoriteLandmarksUseCase()

            when (result) {
                is TResult.Success -> {
                    _state.update { it.copy(isLoading = false, landmarks = result.data) }
                }
                is TResult.Error -> {
                    val errorRes = result.exception?.parseToResource() ?: R.string.error_unknown
                    _state.update { it.copy(isLoading = false, error = errorRes) }
                    _event.emit(FavoritesEvent.ShowToast(errorRes))
                }
            }
        }
    }

    private fun removeFavourite(landmarkId: String) {
        viewModelScope.launch {
            val result = toggleFavoriteUseCase(landmarkId, isFavourite = false)
            if (result is TResult.Success) {
                loadFavorites()
            } else {
                _event.emit(FavoritesEvent.ShowToast(R.string.error_unknown))
            }
        }
    }

    private fun navigateToDetails(landmarkId: String, source: LandmarkSource) {
        viewModelScope.launch {
            _event.emit(FavoritesEvent.NavigateToDetails(landmarkId, source))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _event.emit(FavoritesEvent.NavigateBack)
        }
    }
}