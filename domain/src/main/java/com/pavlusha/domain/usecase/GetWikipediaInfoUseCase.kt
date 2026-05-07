package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.WikipediaInfoDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkSearchRepository

class GetWikipediaInfoUseCase(
    private val searchRepository: ILandmarkSearchRepository
) {
    suspend operator fun invoke(
        landmarkName: String
    ): TResult<WikipediaInfoDomainModel, AppExceptionDomainModel> {
        if (landmarkName.isBlank()) {
            return TResult.Error(AppExceptionDomainModel.Other(Exception("Name is empty")))
        }

        return searchRepository.getWikipediaDescription(landmarkName)
    }
}