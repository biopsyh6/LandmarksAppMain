package com.pavlusha.data.repository

import com.pavlusha.data.mapper.LocationDataMapper
import com.pavlusha.data.remote.YandexLocationDataSource
import com.pavlusha.domain.model.UserLocationDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILocationRepository
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

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