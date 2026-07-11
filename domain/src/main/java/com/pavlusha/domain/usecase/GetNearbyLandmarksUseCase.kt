package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository

class GetNearbyLandmarksUseCase(
    private val repository: ILandmarkRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        radiusMeters: Float = 500f
    ): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel> =
        repository.getNearbyLandmarks(lat, lon, radiusMeters)
}