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
        return when (source) {
            LandmarkSource.LOCAL_DB -> {
                localRepository.getLandmarkById(id)
            }

            LandmarkSource.REMOTE_API -> {
                searchRepository.getExternalLandmarkDetails(id, source)
            }

            LandmarkSource.USER_ADDED -> {
                localRepository.getLandmarkById(id)
            }
        }
    }
}