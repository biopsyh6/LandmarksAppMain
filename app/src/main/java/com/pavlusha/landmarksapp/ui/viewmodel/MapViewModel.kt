package com.pavlusha.landmarksapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.usecase.GetUserLocationUseCase
import com.pavlusha.domain.usecase.ObserveUserLocationUseCase
import com.pavlusha.domain.usecase.SearchLandmarksAtPointUseCase
import com.pavlusha.domain.usecase.SearchLandmarksUseCase
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.MapEvent
import com.pavlusha.landmarksapp.ui.intent.MapIntent
import com.pavlusha.landmarksapp.ui.state.MapState
import com.pavlusha.landmarksapp.util.parseToResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val observeUserLocationUseCase: ObserveUserLocationUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val searchLandmarksUseCase: SearchLandmarksUseCase,
    private val searchLandmarksAtPointUseCase: SearchLandmarksAtPointUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<MapEvent>(viewModelScope)
    val event = _event.flow

    private var locationObservationJob: Job? = null

    fun onIntent(intent: MapIntent) {
        when(intent) {
            is MapIntent.OnMapInitialized -> handleMapInitialized()
            is MapIntent.OnMyLocationClicked -> handleMyLocationClicked()
            is MapIntent.StartTracking -> _state.update { it.copy(isTrackingActive = true) }
            is MapIntent.StopTracking -> _state.update { it.copy(isTrackingActive = false) }
            is MapIntent.OnMapCameraMoved -> handleCameraMoved()
            is MapIntent.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = intent.query) }
            }
            is MapIntent.OnSearchExecute -> handleTextSearch()
            is MapIntent.OnSearchNearby -> handleNearbySearch()
            is MapIntent.OnLandmarkClicked -> {
                _state.update { it.copy(selectedLandmark = intent.landmark) }
            }
            is MapIntent.OnCloseLandmarkInfo -> {
                _state.update { it.copy(selectedLandmark = null) }
            }
            is MapIntent.OnLandmarkDetailsClicked -> handleLandmarkDetailsClicked(intent.landmark)
        }
    }

    private fun handleTextSearch() {
        val query = state.value.searchQuery
        if (query.isBlank()) return

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val location = state.value.userLocation
            val result = searchLandmarksUseCase(query, location)

            when (result) {
                is TResult.Success -> {
                    _state.update { it.copy(mapLandmarks = result.data, isLoading = false) }
                }
                is TResult.Error -> {
                    val errorRes = result.exception?.parseToResource()
                        ?: R.string.error_unknown

                    _state.update { it.copy(isLoading = false, error = errorRes) }
                    _event.emit(MapEvent.ShowToast(errorRes))
                }
            }
        }
    }

    private fun handleNearbySearch() {
        val location = state.value.userLocation ?: return

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = searchLandmarksAtPointUseCase(location.latitude, location.longitude)

            when (result) {
                is TResult.Success -> {
                    _state.update { it.copy(mapLandmarks = result.data, isLoading = false) }
                }
                is TResult.Error -> {
                    val errorRes = result.exception?.parseToResource()
                        ?: R.string.error_unknown

                    _state.update { it.copy(isLoading = false, error = errorRes) }
                    _event.emit(MapEvent.ShowToast(errorRes))
                }
            }
        }
    }

    private fun handleMapInitialized() {
        if (_state.value.isMapInitialized) return

        _state.update { it.copy(isMapInitialized = true) }
        startObservationLocation()
    }

    private fun startObservationLocation() {
        locationObservationJob?.cancel()
        locationObservationJob = viewModelScope.launch {
            observeUserLocationUseCase()
                .onStart {
                    _state.update { it.copy(isLoading = true, error = null) }
                }
                .catch { exception ->
                    val errorRes = (exception as? AppExceptionDomainModel)?.parseToResource()
                        ?: R.string.error_unknown

                    _state.update { it.copy(isLoading = false, error = errorRes) }
                    _event.emit(MapEvent.ShowToast(errorRes))
                }
                .collect { location ->
                    _state.update {
                        it.copy(
                            userLocation = location,
                            isLoading = false,
                            isTrackingActive = if (it.isFirstLocationFix) true else it.isTrackingActive,
                            isFirstLocationFix = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun handleMyLocationClicked() {
        _state.update { it.copy(isTrackingActive = true, isLoading = true) }

        viewModelScope.launch {
            val location = getUserLocationUseCase()
            if (location != null) {
                _state.update {
                    it.copy(userLocation = location, isLoading = false)
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun handleCameraMoved() {
        if (state.value.isTrackingActive) {
            _state.update { it.copy(isTrackingActive = false) }
        }
    }

    private fun handleLandmarkDetailsClicked(landmark: SearchResultDomainModel) {
        viewModelScope.launch {
            _event.emit(MapEvent.NavigateToLandmarkDetails(landmark.id, landmark.source))
        }
    }
}