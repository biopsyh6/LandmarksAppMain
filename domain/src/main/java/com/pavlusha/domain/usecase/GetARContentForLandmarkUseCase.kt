package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.ARContentDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IARContentRepository

class GetARContentForLandmarkUseCase(
    private val arContentRepository: IARContentRepository
) {
    suspend operator fun invoke(
        landmarkId: String
    ): TResult<ARContentDomainModel, AppExceptionDomainModel> {
        if (landmarkId.isBlank()) {
            return TResult.Error(
                AppExceptionDomainModel.Other(Exception("Landmark ID cannot be empty"))
            )
        }
        return arContentRepository.getARContentForLandmark(landmarkId)
    }
}