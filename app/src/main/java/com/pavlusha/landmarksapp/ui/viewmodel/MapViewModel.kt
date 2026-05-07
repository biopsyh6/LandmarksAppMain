package com.pavlusha.landmarksapp.ui.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.usecase.GetPedestrianRouteUseCase
import com.pavlusha.domain.usecase.GetUserLocationUseCase
import com.pavlusha.domain.usecase.ObserveUserLocationUseCase
import com.pavlusha.domain.usecase.SearchLandmarksAtPointUseCase
import com.pavlusha.domain.usecase.SearchLandmarksUseCase
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.MapEvent
import com.pavlusha.landmarksapp.ui.intent.MapIntent
import com.pavlusha.landmarksapp.ui.state.CameraPositionData
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
    private val getPedestrianRouteUseCase: GetPedestrianRouteUseCase,
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
            is MapIntent.OnMapCameraMoved -> {
                _state.update {
                    it.copy(
                        lastCameraPosition = CameraPositionData(
                            latitude = intent.latitude,
                            longitude = intent.longitude,
                            zoom = intent.zoom
                        )
                    )
                }
            }
            is MapIntent.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = intent.query) }
            }
            is MapIntent.OnSearchExecute -> handleTextSearch()
            is MapIntent.OnSearchNearby -> handleNearbySearch()
            is MapIntent.OnLandmarkClicked -> {
                val loc = state.value.userLocation
                val dist = if (loc != null) {
                    val results = FloatArray(1)
                    Location.distanceBetween(loc.latitude, loc.longitude, intent.landmark.latitude, intent.landmark.longitude, results)
                    results[0]
                } else null
                _state.update { it.copy(selectedLandmark = intent.landmark, distanceToSelected = dist) }
            }
            is MapIntent.OnCloseLandmarkInfo -> {
                _state.update { it.copy(selectedLandmark = null, currentRoute = null) }
            }
            is MapIntent.OnLandmarkDetailsClicked -> handleLandmarkDetailsClicked(intent.landmark)
            is MapIntent.OnBuildRouteClicked -> handleBuildRouteClicked()
            is MapIntent.OnCancelRouteClicked -> {
                _state.update { it.copy(currentRoute = null) }
            }
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
                    val currentState = _state.value
                    var newDistance: Float? = currentState.distanceToSelected
                    var routeFinished = false

                    currentState.selectedLandmark?.let { target ->
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            location.latitude, location.longitude,
                            target.latitude, target.longitude,
                            results
                        )
                        newDistance = results[0]

                        if (currentState.currentRoute != null && newDistance!! < 20f) {
                            routeFinished = true
                        }
                    }

                    _state.update {
                        it.copy(
                            userLocation = location,
                            distanceToSelected = newDistance,
                            currentRoute = if (routeFinished) null else it.currentRoute,
                            isLoading = false,
                            isTrackingActive = if (it.isFirstLocationFix) true else it.isTrackingActive,
                            isFirstLocationFix = false,
                            error = null
                        )
                    }

                    if (routeFinished) {
                        _event.emit(MapEvent.ShowToast(R.string.arrived_message))
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

    private fun handleLandmarkDetailsClicked(landmark: SearchResultDomainModel) {
        viewModelScope.launch {
            _event.emit(MapEvent.NavigateToLandmarkDetails(landmark.id, landmark.source))
        }
    }

    private fun handleBuildRouteClicked() {
        val destination = state.value.selectedLandmark ?: return
        val startLoc = state.value.userLocation

        if (startLoc == null) {
            viewModelScope.launch {
                _event.emit(MapEvent.ShowToast(R.string.error_unknown))
            }
            return
        }

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = getPedestrianRouteUseCase(
                startLat = startLoc.latitude,
                startLon = startLoc.longitude,
                endLat = destination.latitude,
                endLon = destination.longitude
            )

            when (result) {
                is TResult.Success -> {
                    _state.update { it.copy(currentRoute = result.data, isLoading = false) }
                }
                is TResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(MapEvent.ShowToast(R.string.error_unknown))
                }
            }
        }
    }
}