package com.pavlusha.data.repository

import com.pavlusha.data.mapper.LocationDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.domain.model.UserLocationDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILocationRepository
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.location.SubscriptionSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepositoryImpl : ILocationRepository {

    private val locationManager by lazy {
        MapKitFactory.getInstance().createLocationManager()
    }

    override fun observeUserLocation(): Flow<UserLocationDomainModel> = callbackFlow {
        val listener = object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                val domainModel = LocationDataMapper.toDomainFromYandex(location)
                trySend(domainModel)
            }

            override fun onLocationStatusUpdated(status: LocationStatus) {
                when (status) {
                    LocationStatus.NOT_AVAILABLE -> {
                        val exception = Exception("Location services are disabled")
                        close(AppExceptionDomainModel.LocationDisabled(exception))
                    }

                    LocationStatus.AVAILABLE -> {

                    }

                    LocationStatus.RESET -> {

                    }
                }
            }

        }

        try {
            locationManager.subscribeForLocationUpdates(SubscriptionSettings(), listener)
        } catch (e: Exception) {
            close(e.toAppExceptionDomainModel())
        }

        awaitClose {
            locationManager.unsubscribe(listener)
        }
    }


    override suspend fun getCurrentLocation(): UserLocationDomainModel? =
        suspendCancellableCoroutine { continuation ->
            val listener = object : LocationListener {
                override fun onLocationUpdated(location: Location) {
                    if (continuation.isActive) {
                        continuation.resume(LocationDataMapper.toDomainFromYandex(location))
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