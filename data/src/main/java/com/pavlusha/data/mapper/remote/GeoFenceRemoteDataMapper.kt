package com.pavlusha.data.mapper.remote

import com.pavlusha.data.model.remote.GeoFenceRemoteModel
import com.pavlusha.domain.model.GeoFenceDomainModel

object GeoFenceRemoteDataMapper {
    fun toDomainFromData(remote: GeoFenceRemoteModel): GeoFenceDomainModel {
        return GeoFenceDomainModel(
            landmarkId = remote.landmarkId,
            latitude = remote.latitude,
            longitude = remote.longitude,
            radiusMeters = remote.radiusMeters
        )
    }

    fun fromDomainToData(domain: GeoFenceDomainModel): GeoFenceRemoteModel {
        return GeoFenceRemoteModel(
            landmarkId = domain.landmarkId,
            latitude = domain.latitude,
            longitude = domain.longitude,
            radiusMeters = domain.radiusMeters
        )
    }
}