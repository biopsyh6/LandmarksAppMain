package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.ILandmarkSearchRepository

class GetLandmarkDetailsUseCase(
    private val localRepository: ILandmarkRepository,
    private val searchRepository: ILandmarkSearchRepository
) {
    suspend operator fun invoke(
        id: String,
        source: LandmarkSource
    ): TResult<LandmarkDomainModel, AppExceptionDomainModel> {
        val primaryResult = localRepository.getLandmarkById(id)

        if (primaryResult is TResult.Success) {
            return primaryResult
        }

        if (source == LandmarkSource.REMOTE_API) {
            return searchRepository.getExternalLandmarkDetails(id, source)
        }

        return primaryResult
    }
}