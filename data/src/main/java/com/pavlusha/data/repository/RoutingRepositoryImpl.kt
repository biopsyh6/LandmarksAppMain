package com.pavlusha.data.repository

import com.pavlusha.data.mapper.RouteDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.remote.YandexRoutingDataSource
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RouteDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IRoutingRepository
import com.yandex.mapkit.geometry.Point

class RoutingRepositoryImpl(
    private val routingDataSource: YandexRoutingDataSource
): IRoutingRepository {
    override suspend fun getPedestrianRoute(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ): TResult<RouteDomainModel?, AppExceptionDomainModel> {
        return try {
            val yandexPolyline = routingDataSource.fetchPedestrianRoute(
                Point(startLat, startLon),
                Point(endLat, endLon)
            )

            if (yandexPolyline != null) {
                val domainRoute = RouteDataMapper.toDomainFromYandexPolyline(yandexPolyline)
                TResult.Success(domainRoute)
            } else {
                TResult.Success(null)
            }

        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }
}