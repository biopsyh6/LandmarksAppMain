package com.pavlusha.landmarksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pavlusha.landmarksapp.Destination
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.screens.common.currentRoute

@Composable
fun MainTabsScreen(rootNavController: NavController) {
    val tabsNavController = rememberNavController()
    val currentRoute = currentRoute(tabsNavController)
    val destinations = Destination.entries

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(R.color.white),
                modifier = Modifier.height(85.dp)
            ) {
                destinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route && destination != Destination.RECOGNITION,
                        onClick = {
                            if (destination == Destination.RECOGNITION) {
                                rootNavController.navigate("ar_recognition")
                            } else {
                                tabsNavController.navigate(destination.route) {
                                    popUpTo(tabsNavController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            NavigationIconWithIndicator(
                                isSelected = currentRoute == destination.route && destination != Destination.RECOGNITION,
                                destination = destination
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = tabsNavController,
            startDestination = Destination.EXPLORE.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Destination.EXPLORE.route) {
                ExploreMapScreen(rootNavController, paddingValues)
            }
//            composable(Destination.FAVOURITES.route) {
//                FavoriteLandmarksScreen(rootNavController)
//            }
            composable(Destination.PROFILE.route) {
                ProfileScreen(rootNavController)
            }
        }
    }
}


@Composable
fun NavigationIconWithIndicator(
    isSelected: Boolean,
    destination: Destination
) {
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-16).dp)
                    .height(3.dp)
                    .width(24.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colorResource(id = R.color.red))
            )
        }

        Icon(
            painter = if (isSelected) {
                destination.activeIcon()
            } else {
                destination.inactiveIcon()
            },
            contentDescription = destination.contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
    }
}
