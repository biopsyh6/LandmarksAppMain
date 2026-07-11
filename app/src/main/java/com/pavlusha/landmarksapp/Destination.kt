package com.pavlusha.landmarksapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

enum class Destination(
    val route: String,
    val label: String,
    val inactiveIcon: @Composable () -> Painter,
    val activeIcon: @Composable () -> Painter,
    val contentDescription: String
) {
    EXPLORE(
        route = "explore",
        label = "Explore",
        inactiveIcon = { rememberVectorPainter(Icons.Outlined.Map) },
        activeIcon = { rememberVectorPainter(Icons.Filled.Map) },
        contentDescription = "Search Landmarks",
    ),
    RECOGNITION(
        route = "recognition",
        label = "Recognition",
        inactiveIcon = { rememberVectorPainter(Icons.Outlined.CameraAlt) },
        activeIcon = { rememberVectorPainter(Icons.Filled.CameraAlt) },
        contentDescription = "Camera for recognise"
    ),
    PROFILE(
        route = "profile",
        label = "Profile",
        inactiveIcon = { rememberVectorPainter(Icons.Outlined.Person) },
        activeIcon = { rememberVectorPainter(Icons.Filled.Person) },
        contentDescription = "My profile"
    )
}