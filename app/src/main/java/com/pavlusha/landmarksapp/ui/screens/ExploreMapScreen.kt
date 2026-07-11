package com.pavlusha.landmarksapp.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.pavlusha.data.mapper.RouteDataMapper
import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.event.MapEvent
import com.pavlusha.landmarksapp.ui.intent.MapIntent
import com.pavlusha.landmarksapp.ui.screens.common.formatDistance
import com.pavlusha.landmarksapp.ui.viewmodel.MapViewModel
import com.pavlusha.landmarksapp.util.vectorToBitmap
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import org.koin.androidx.compose.koinViewModel


fun hasLocationPermissions(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarse = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    return fine && coarse
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreMapScreen(
    navController: NavController,
    parentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: MapViewModel = koinViewModel()
) {
    val context = LocalContext.current

    var userLocationLayer by remember { mutableStateOf<UserLocationLayer?>(null) }
    var isPermissionsGranted by remember { mutableStateOf(hasLocationPermissions(context)) }
    var isMapStarted by remember { mutableStateOf(false) }

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            isPermissionsGranted = true
        } else {
            Toast.makeText(context, "Геолокация необходима для работы карты", Toast.LENGTH_LONG)
                .show()
        }
    }

    LaunchedEffect(Unit) {
        if (!isPermissionsGranted) {
            permissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(isPermissionsGranted) {
        if (isPermissionsGranted) {
            viewModel.onIntent(MapIntent.OnMapInitialized)
        }
    }

    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }

    var mapObjectsCollection by remember { mutableStateOf<MapObjectCollection?>(null) }
    var routeObjectsCollection by remember { mutableStateOf<MapObjectCollection?>(null) }

    val tapListener = remember {
        MapObjectTapListener { mapObject, _ ->
            val landmarkData = mapObject.userData as? SearchResultDomainModel
            if (landmarkData != null) {
                viewModel.onIntent(MapIntent.OnLandmarkClicked(landmarkData))
                true
            } else {
                false
            }
        }
    }

    val cameraListener = remember {
        CameraListener { _, cameraPosition, cameraUpdateReason, _ ->
            viewModel.onIntent(
                MapIntent.OnMapCameraMoved(
                    latitude = cameraPosition.target.latitude,
                    longitude = cameraPosition.target.longitude,
                    zoom = cameraPosition.zoom
                )
            )

            if (cameraUpdateReason == CameraUpdateReason.GESTURES) {
                viewModel.onIntent(MapIntent.StopTracking)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                    isMapStarted = true
                }

                Lifecycle.Event.ON_STOP -> {
                    isMapStarted = false
                    mapView.onStop()
                    MapKitFactory.getInstance().onStop()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.mapWindow.map.removeCameraListener(cameraListener)
        }
    }

    LaunchedEffect(isMapStarted, isPermissionsGranted) {
        if (isMapStarted && isPermissionsGranted && userLocationLayer == null) {
            try {
                val mapKit = MapKitFactory.getInstance()
                userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
                    isVisible = true
                    isHeadingModeActive = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is MapEvent.ShowToast -> {
                    val text = context.resources.getString(event.message)
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                }

                is MapEvent.NavigateToLandmarkDetails -> {
                    val encodedId = Uri.encode(event.landmarkId)
                    navController.navigate("details_screen/$encodedId/${event.source.name}")
                }
            }
        }
    }

    LaunchedEffect(state.userLocation, state.isTrackingActive, isMapStarted) {
        val location = state.userLocation

        if (location != null && state.isTrackingActive && isMapStarted) {

            kotlinx.coroutines.delay(100)

            mapView.mapWindow.map.move(
                CameraPosition(
                    Point(location.latitude, location.longitude),
                    16.0f,
                    0.0f,
                    0.0f
                ),
                Animation(Animation.Type.SMOOTH, 0.8f),
                null
            )
        }
    }

    fun changeZoom(delta: Float) {
        val map = mapView.mapWindow.map
        val currentPosition = map.cameraPosition
        val newZoom = (currentPosition.zoom + delta).coerceIn(2.0f, 21.0f)
        map.move(
            CameraPosition(
                currentPosition.target,
                newZoom,
                currentPosition.azimuth,
                currentPosition.tilt
            ),
            Animation(Animation.Type.SMOOTH, 0.2f),
            null
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = parentPadding.calculateBottomPadding()),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(MapIntent.OnMyLocationClicked) },
                containerColor = colorResource(id = R.color.red),
                contentColor = colorResource(id = R.color.white)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Где я?"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    mapView.mapWindow.map.addCameraListener(cameraListener)
                    mapObjectsCollection = mapView.mapWindow.map.mapObjects.addCollection()
                    routeObjectsCollection = mapView.mapWindow.map.mapObjects.addCollection()

                    val lastPos = state.lastCameraPosition
                    if (lastPos != null) {
                        mapView.mapWindow.map.move(
                            CameraPosition(
                                Point(lastPos.latitude, lastPos.longitude),
                                lastPos.zoom,
                                0f,
                                0f
                            )
                        )
                    }
                    mapView
                },
                update = { view ->
                    val location = state.userLocation

                    mapObjectsCollection?.let { collection ->
                        collection.clear()

                        val iconBitmap = vectorToBitmap(context, R.drawable.outline_location_landmark_24, 36)
                        val imageProvider = ImageProvider.fromBitmap(iconBitmap)

                        state.mapLandmarks.forEach { landmark ->
                            val point = Point(landmark.latitude, landmark.longitude)

                            val placemark = collection.addPlacemark(point)

                            val iconStyle = com.yandex.mapkit.map.IconStyle().apply {
                                anchor = android.graphics.PointF(0.5f, 1.0f)
                            }
                            placemark.setIcon(imageProvider, iconStyle)

                            placemark.userData = landmark
                            placemark.addTapListener(tapListener)
                        }
                    }

                    routeObjectsCollection?.let { routeCollection ->
                        routeCollection.clear()

                        state.currentRoute?.let { routeDomainModel ->
                            val polyline = RouteDataMapper.toYandexPolylineFromDomain(routeDomainModel)

                            val routeObj = routeCollection.addPolyline(polyline)
                            routeObj.setStrokeColor(android.graphics.Color.BLUE)
                            routeObj.strokeWidth = 4f
                            routeObj.setOutlineColor(android.graphics.Color.WHITE)
                            routeObj.outlineWidth = 1f
                        }
                    }
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            ) {
                SearchBarUI(
                    query = state.searchQuery,
                    onQueryChange = { viewModel.onIntent(MapIntent.OnSearchQueryChanged(it)) },
                    onSearch = { viewModel.onIntent(MapIntent.OnSearchExecute) },
                    onNearbyClick = { viewModel.onIntent(MapIntent.OnSearchNearby) }
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ZoomButton(icon = Icons.Default.Add, onClick = { changeZoom(1f) })
                ZoomButton(icon = Icons.Default.Remove, onClick = { changeZoom(-1f) })
            }

            if (state.currentRoute != null && state.selectedLandmark != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 100.dp, start = 16.dp, end = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = colorResource(id = R.color.red),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Идем к: ${state.selectedLandmark!!.name}", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Осталось: ${formatDistance(state.distanceToSelected)}", color = Color.White)
                        }
                        IconButton(onClick = { viewModel.onIntent(MapIntent.OnCancelRouteClicked) }) {
                            Icon(Icons.Default.Close, contentDescription = "Отменить маршрут", tint = Color.White)
                        }
                    }
                }
            }

            if (state.selectedLandmark != null) {
                LandmarkInfoCard(
                    landmark = state.selectedLandmark!!,
                    distance = state.distanceToSelected,
                    onClose = { viewModel.onIntent(MapIntent.OnCloseLandmarkInfo) },
                    onDetailsClick = { viewModel.onIntent(MapIntent.OnLandmarkDetailsClicked(state.selectedLandmark!!)) },
                    onBuildRouteClick = { viewModel.onIntent(MapIntent.OnBuildRouteClicked) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = innerPadding.calculateBottomPadding() + 16.dp)
                )
            }

            if (state.isLoading && state.userLocation == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colorResource(id = R.color.red)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarUI(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onNearbyClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Поиск...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            if (query.isNotEmpty()) {
                IconButton(onClick = onSearch) {
                    Icon(Icons.Default.Search, contentDescription = "Найти")
                }
            } else {
                IconButton(onClick = onNearbyClick) {
                    Icon(Icons.Default.Place, contentDescription = "Поиск рядом")
                }
            }
        }
    }
}

@Composable
fun LandmarkInfoCard(
    landmark: SearchResultDomainModel,
    distance: Float?,
    onClose: () -> Unit,
    onDetailsClick: () -> Unit,
    onBuildRouteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = landmark.name,
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Закрыть")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = landmark.address ?: "Адрес не указан",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = formatDistance(distance),
                    style = MaterialTheme.typography.labelLarge,
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val displayDescription = landmark.descriptionFromCategories ?: "Достопримечательность"

            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = displayDescription,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onBuildRouteClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Маршрут", color = colorResource(id = R.color.black))
                }

                Button(
                    onClick = onDetailsClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.red)
                    )
                ) {
                    Text(text = "Подробнее", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ZoomButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.9f),
        shadowElevation = 4.dp,
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(id = R.color.black),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
