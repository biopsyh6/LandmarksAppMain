package com.pavlusha.data.mapper

import com.pavlusha.data.model.UserLocationDataModel
import com.pavlusha.domain.model.UserLocationDomainModel
import com.yandex.mapkit.location.Location

object LocationDataMapper {
    fun fromYandexToData(yandexLocation: Location): UserLocationDataModel {
        return UserLocationDataModel(
            latitude = yandexLocation.position.latitude,
            longitude = yandexLocation.position.longitude,
            accuracy = yandexLocation.accuracy?.toFloat(),
            altitude = yandexLocation.altitude,
            speed = yandexLocation.speed?.toFloat(),
            bearing = yandexLocation.heading?.toFloat(),
            timestamp = yandexLocation.absoluteTimestamp
        )
    }

    fun toDomainFromData(data: UserLocationDataModel): UserLocationDomainModel {
        return UserLocationDomainModel(
            latitude = data.latitude,
            longitude = data.longitude,
            accuracy = data.accuracy ?: 0f,
            bearing = data.bearing,
            timestamp = data.timestamp
        )
    }

    fun toDomainFromYandex(yandexLocation: Location): UserLocationDomainModel =
        toDomainFromData(fromYandexToData(yandexLocation))
}