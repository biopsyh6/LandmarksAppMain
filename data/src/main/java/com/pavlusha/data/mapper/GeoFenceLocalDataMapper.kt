package com.pavlusha.data.mapper

import com.pavlusha.data.local.entity.GeoFenceEntity
import com.pavlusha.domain.model.GeoFenceDomainModel

object GeoFenceLocalDataMapper {
    fun toDomainFromData(entity: GeoFenceEntity): GeoFenceDomainModel {
        return GeoFenceDomainModel(
            landmarkId = entity.landmarkId,
            latitude = entity.latitude,
            longitude = entity.longitude,
            radiusMeters = entity.radiusMeters
        )
    }

    fun fromDomainToData(domain: GeoFenceDomainModel): GeoFenceEntity {
        return GeoFenceEntity(
            landmarkId = domain.landmarkId,
            latitude = domain.latitude,
            longitude = domain.longitude,
            radiusMeters = domain.radiusMeters
        )
    }
}