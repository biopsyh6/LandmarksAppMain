package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.IUserContentRepository

class GetNotesForLandmarkUseCase(
    private val repository: IUserContentRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(
        landmarkId: String
    ): TResult<List<UserNoteDomainModel>, AppExceptionDomainModel> {
        val user = authRepository.getCurrentUser()

        if (user == null) {
            return TResult.Error(AppExceptionDomainModel.NoAuth(Exception("User not logged in")))
        }

        return repository.getNotesForLandmark(landmarkId, user.id)
    }
}