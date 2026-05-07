package com.pavlusha.data.mapper

import com.pavlusha.domain.model.GeoCoordinateDomainModel
import com.pavlusha.domain.model.RouteDomainModel
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline

object RouteDataMapper {
    fun toDomainFromYandexPolyline(polyline: Polyline): RouteDomainModel {
        val domainPoints = polyline.points.map { yandexPoint ->
            GeoCoordinateDomainModel(
                latitude = yandexPoint.latitude,
                longitude = yandexPoint.longitude
            )
        }
        return RouteDomainModel(points = domainPoints)
    }

    fun toYandexPolylineFromDomain(domainRoute: RouteDomainModel): Polyline {
        val yandexPoints = domainRoute.points.map { domainPoint ->
            Point(domainPoint.latitude, domainPoint.longitude)
        }
        return Polyline(yandexPoints)
    }
}