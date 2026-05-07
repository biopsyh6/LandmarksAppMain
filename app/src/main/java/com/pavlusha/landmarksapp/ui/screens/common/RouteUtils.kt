package com.pavlusha.landmarksapp.ui.screens.common

fun formatDistance(meters: Float?): String {
    if (meters == null) return "Вычисляем..."
    return if (meters < 1000) {
        "${meters.toInt()} м"
    } else {
        String.format("%.1f км", meters / 1000f)
    }
}