package com.pavlusha.landmarksapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
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
        inactiveIcon = { painterResource(id = R.drawable.ic_launcher_foreground) },
        activeIcon = { painterResource(id = R.drawable.ic_launcher_foreground) },
        contentDescription = "Search Landmarks",
    ),
    FAVOURITES(
        route = "favourites",
        label = "Favourites",
        inactiveIcon = { painterResource(id = R.drawable.ic_launcher_foreground) },
        activeIcon = { painterResource(id = R.drawable.ic_launcher_foreground) },
        contentDescription = "Saved places"
    ),
    PROFILE(
        route = "profile",
        label = "Profile",
        inactiveIcon = { painterResource(id = R.drawable.ic_launcher_foreground) },
        activeIcon = { painterResource(id = R.drawable.ic_launcher_foreground) },
        contentDescription = "My profile"
    )
}