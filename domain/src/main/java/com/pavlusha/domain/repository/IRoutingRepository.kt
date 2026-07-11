package com.pavlusha.domain.repository

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RouteDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel

interface IRoutingRepository {
    suspend fun getPedestrianRoute(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ): TResult<RouteDomainModel?, AppExceptionDomainModel>
}