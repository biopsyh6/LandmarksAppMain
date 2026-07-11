package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RouteDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IRoutingRepository

class GetPedestrianRouteUseCase(
    private val routingRepository: IRoutingRepository
) {
    suspend operator fun invoke(
        startLat: Double, startLon: Double,
        endLat: Double, endLon: Double
    ): TResult<RouteDomainModel?, AppExceptionDomainModel> {
        return routingRepository.getPedestrianRoute(
            startLat = startLat,
            startLon = startLon,
            endLat = endLat,
            endLon = endLon
        )
    }
}