package com.pavlusha.data.remote

import com.pavlusha.data.mapper.LocationDataMapper
import com.pavlusha.data.model.UserLocationDataModel
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.location.Purpose
import com.yandex.mapkit.location.SubscriptionSettings
import com.yandex.mapkit.location.UseInBackground
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class YandexLocationDataSource(
    private val locationManager: LocationManager
) {
    fun observeLocation(): Flow<UserLocationDataModel> = callbackFlow {
        val listener = object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                trySend(LocationDataMapper.fromYandexToData(location))
            }

            override fun onLocationStatusUpdated(status: LocationStatus) {
                if (status == LocationStatus.NOT_AVAILABLE) {
                    close(Exception("Location services are disabled"))
                }
            }
        }

        try {
            val settings = SubscriptionSettings(
                UseInBackground.ALLOW,
                Purpose.PEDESTRIAN_NAVIGATION
            )

            locationManager.subscribeForLocationUpdates(settings, listener)
        } catch (e: Exception) {
            close(e)
        }

        awaitClose {
            locationManager.unsubscribe(listener)
        }
    }

    suspend fun getCurrentLocation(): UserLocationDataModel? =
        suspendCancellableCoroutine { continuation ->
            val listener = object : LocationListener {
                override fun onLocationUpdated(location: Location) {
                    if (continuation.isActive) {
                        continuation.resume(LocationDataMapper.fromYandexToData(location))
                    }
                    locationManager.unsubscribe(this)
                }

                override fun onLocationStatusUpdated(status: LocationStatus) {
                    if (status == LocationStatus.NOT_AVAILABLE && continuation.isActive) {
                        continuation.resume(null)
                        locationManager.unsubscribe(this)
                    }
                }
            }

            try {
                locationManager.requestSingleUpdate(listener)
            } catch (e: Exception) {
                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }

            continuation.invokeOnCancellation {
                locationManager.unsubscribe(listener)
            }
        }
}