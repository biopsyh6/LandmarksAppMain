package com.pavlusha.landmarksapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.VisitWithLandmarkDomainModel
import com.pavlusha.domain.usecase.GetVisitHistoryWithLandmarksUseCase
import com.pavlusha.domain.usecase.GetWikipediaInfoUseCase
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.VisitHistoryEvent
import com.pavlusha.landmarksapp.ui.intent.VisitHistoryIntent
import com.pavlusha.landmarksapp.ui.state.VisitHistoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VisitHistoryViewModel(
    private val getVisitHistoryWithLandmarksUseCase: GetVisitHistoryWithLandmarksUseCase,
    private val getWikipediaInfoUseCase: GetWikipediaInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VisitHistoryState())
    val state: StateFlow<VisitHistoryState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<VisitHistoryEvent>(viewModelScope)
    val event = _event.flow

    init {
        onIntent(VisitHistoryIntent.LoadHistory)
    }

    fun onIntent(intent: VisitHistoryIntent) {
        when (intent) {
            is VisitHistoryIntent.LoadHistory -> loadHistory()
            is VisitHistoryIntent.OnLandmarkClicked -> navigateToDetails(intent.landmarkId, intent.source)
            is VisitHistoryIntent.OnBackClicked -> navigateBack()
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = getVisitHistoryWithLandmarksUseCase()

            when (result) {
                is TResult.Success -> {
                    _state.update { it.copy(isLoading = false, visits = result.data) }
                    loadWikipediaImagesForList(result.data)
                }
                is TResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(VisitHistoryEvent.ShowToast("Не удалось загрузить историю"))
                }
            }
        }
    }

    private fun loadWikipediaImagesForList(visits: List<VisitWithLandmarkDomainModel>) {
        visits.forEach { item ->
            val landmark = item.landmark
            if (!landmark.localMainImagePath.isNullOrEmpty()) return@forEach

            viewModelScope.launch {
                val wikiResult = getWikipediaInfoUseCase(landmark.name)

                if (wikiResult is TResult.Success) {
                    val imageUrl = wikiResult.data.thumbnailUrl ?: wikiResult.data.originalImageUrl

                    if (imageUrl != null) {
                        _state.update { currentState ->
                            val updatedList = currentState.visits.map { currentItem ->
                                if (currentItem.visit.id == item.visit.id) {
                                    currentItem.copy(
                                        landmark = currentItem.landmark.copy(thumbnailUrl = imageUrl)
                                    )
                                } else {
                                    currentItem
                                }
                            }
                            currentState.copy(visits = updatedList)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDetails(landmarkId: String, source: LandmarkSource) {
        viewModelScope.launch {
            _event.emit(VisitHistoryEvent.NavigateToDetails(landmarkId, source))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _event.emit(VisitHistoryEvent.NavigateBack)
        }
    }
}