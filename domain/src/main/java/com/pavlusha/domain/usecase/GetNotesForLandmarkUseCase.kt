package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IUserContentRepository

class GetNotesForLandmarkUseCase(
    private val repository: IUserContentRepository
) {
    suspend operator fun invoke(
        landmarkId: String
    ): TResult<List<UserNoteDomainModel>, AppExceptionDomainModel> =
        repository.getNotesForLandmark(landmarkId)
}