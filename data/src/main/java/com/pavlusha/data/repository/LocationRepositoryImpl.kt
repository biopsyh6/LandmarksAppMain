package com.pavlusha.data.repository

import com.pavlusha.data.mapper.LocationDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.remote.YandexLocationDataSource
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepositoryImpl(
    private val locationDataSource: YandexLocationDataSource
) : ILocationRepository {

    private val locationManager by lazy {
        MapKitFactory.getInstance().createLocationManager()
    }

    override fun observeUserLocation(): Flow<UserLocationDomainModel> {
        return locationDataSource.observeLocation()
            .map { dataModel ->
                LocationDataMapper.toDomainFromData(dataModel)
            }
            .catch { e ->
                throw AppExceptionDomainModel.LocationDisabled(e as Exception)
            }
    }


    override suspend fun getCurrentLocation(): UserLocationDomainModel? {
        return try {
            val dataModel = locationDataSource.getCurrentLocation()
            dataModel?.let { LocationDataMapper.toDomainFromData(it) }
        } catch (e: Exception) {
            null
        }
    }

}